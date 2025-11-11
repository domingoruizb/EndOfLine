import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import './game.css'
import { boardArray, checkPlacementValid, getCards, getCoordinates, getInitialValidIndexes, getRotation, getValidIndexes, nameToBinary } from './gameUtils/cardUtils'
import { skills } from './gameUtils/skillsUtils'
import tokenService from '../services/token.service'

const jwt = tokenService.getLocalAccessToken()
const user = tokenService.getUser()

export default function GamePage () {
    const { gameId } = useParams()
    const navigate = useNavigate()
    const [selectedCard, setSelectedCard] = useState(null)
    const [cards] = useState(getCards())
    const [board, setBoard] = useState(boardArray)
    const [lastPlacedCard, setLastPlacedCard] = useState(null)
    const [nextValidIndexes, setNextValidIndexes] = useState([])
    const [gameData, setGameData] = useState(null)
    const [elapsed, setElapsed] = useState(0)

    if (gameData != null && gameData.startedAt == null) {
        navigate(`/lobby/${gameId}`)
    }

    const isHost = useMemo(() => {
        const hostId = gameData?.host?.id
        const userId = user?.id
        return hostId != null && userId != null ? hostId === userId : false
    }, [gameData?.host?.id])

    const handleCardSelect = (card) => {
        setSelectedCard(prevCard => (prevCard === card ? null : card))
    }

    const handlePlaceCard = (index) => {
        if (selectedCard == null) {
            return
        }

        if (lastPlacedCard == null) {
            if (isHost == null) {
                return
            }

            if (!getInitialValidIndexes(isHost).includes(index)) {
                return
            }
        }

        if (!checkPlacementValid(board, selectedCard, index, lastPlacedCard)) {
            return
        }

        const rotation = getRotation(index, lastPlacedCard)

        const newBoard = [...board]
        newBoard[index] = { name: selectedCard, rotation }

        setBoard(newBoard)

        const lastPlaced = {
            name: selectedCard,
            index,
            rotation
        }

        setLastPlacedCard(lastPlaced)

        const nextIndexes = getValidIndexes(lastPlaced, board)
        setNextValidIndexes(nextIndexes)
        console.log('Next valid indexes:', nextIndexes)

        if (nextIndexes.length === 0) {
            console.log('Game Over!')
        }

        setSelectedCard(null)
    }

    useEffect(() => {
        const abortController = new AbortController()
        fetch(
            `/api/v1/games/${gameId}`,
            {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((res) => res.json())
            .then(data => {
                setGameData(data)
                console.log(data)
            })
            .catch((message) => console.log(message))

        return () => abortController.abort('Component unmounted')
    }, [gameData?.startedAt, gameId])

    useEffect(() => {
        if (lastPlacedCard == null && isHost != null) {
            const initialIndexes = getInitialValidIndexes(isHost)
            setNextValidIndexes(initialIndexes)
        }
    }, [isHost, lastPlacedCard])

    useEffect(() => {
        if (gameData?.startedAt == null) {
            return
        }

        const startedAt = new Date(gameData.startedAt).getTime()
        const interval = setInterval(() => {
            setElapsed(Date.now() - startedAt)
        }, 1000)

        return () => clearInterval(interval)
    }, [gameData?.startedAt])

    const seconds = Math.floor(elapsed / 1000) % 60
    const minutes = Math.floor(elapsed / 60000) % 60
    const hours = Math.floor(elapsed / 3600000)

    return (
        <div
            className='game-page-container'
        >
            <div
                className='game-data-container'
            >
                Game ID:
                <span>
                    {gameId}
                </span>
                {
                    gameData != null && gameData.host != null && (
                        <>
                            Host:
                            <span>
                                {gameData.host.username}
                            </span>
                            Guest:
                            <span>
                                {
                                    gameData.gamePlayers
                                        .find(player => player.user.id !== gameData.host.id)
                                        ?.user.username ?? 'Waiting for guest...'
                                }
                            </span>
                            Elapsed:
                            <span>
                                {hours.toString().padStart(2, '0')}:
                                {minutes.toString().padStart(2, '0')}:
                                {seconds.toString().padStart(2, '0')}
                            </span>
                        </>
                    )
                }
            </div>
            <div
                className='side-container'
            >
                <div
                    className='skills-container'
                >
                    {
                        skills.map((skill) => (
                            <button
                                key={skill}
                                className='skill-button'
                                onClick={() => console.log(`Activated skill: ${skill}`)}
                            >
                                {skill}
                            </button>
                        ))
                    }
                </div>
                <div
                    className='cards-container'
                >
                    {
                        cards.map((card) => {
                            const bits = nameToBinary(card)

                            return (
                                <button
                                    key={card}
                                    onClick={() => handleCardSelect(card)}
                                    className='card-button'
                                    style={{
                                        borderLeft: (bits & 0b1000) ? '5px solid red' : '5px solid transparent',
                                        borderTop: (bits & 0b0100) ? '5px solid red' : '5px solid transparent',
                                        borderRight: (bits & 0b0010) ? '5px solid red' : '5px solid transparent',
                                        borderBottom: '5px solid yellow',
                                        backgroundColor: selectedCard === card && 'var(--main-orange-color)',
                                        rotate: selectedCard === card && '2.5deg',
                                        transform: selectedCard === card && 'scale(1.05)'
                                    }}
                                >
                                    {card.toUpperCase()}
                                </button>
                            )
                        })
                    }
                </div>
            </div>
            <div
                className='game-board-container'
            >
                <div
                    className='game-board'
                >
                    {
                        board.map((card, index) => {
                            const bits = card != null ? nameToBinary(card.name) : 0b0000
                            const isValid = nextValidIndexes.includes(index)

                            return (
                                <button
                                    key={index}
                                    className='game-cell'
                                    onClick={() => handlePlaceCard(index)}
                                    style={{
                                        rotate: card != null ? `${card.rotation * 90}deg` : '0deg',
                                        borderLeft: (bits & 0b1000) ? '5px solid red' : '5px solid transparent',
                                        borderTop: (bits & 0b0100) ? '5px solid red' : '5px solid transparent',
                                        borderRight: (bits & 0b0010) ? '5px solid red' : '5px solid transparent',
                                        borderBottom: card != null ? '5px solid yellow' : '5px solid transparent',
                                        animationName: isValid ? 'pulsate' : 'none',
                                        animationDuration: isValid ? '2s' : undefined,
                                        animationIterationCount: isValid ? 'infinite' : undefined,
                                        animationFillMode: isValid ? 'both' : undefined
                                    }}
                                >
                                    {
                                        card != null ? card.name.toUpperCase() : (
                                            <span
                                                style={{
                                                    color: 'gray',
                                                }}
                                            >
                                                ({getCoordinates(index).row}, {getCoordinates(index).col})
                                            </span>
                                        )
                                    }
                                </button>
                            )
                        })
                    }
                </div>
            </div> 
        </div>
    )
}
