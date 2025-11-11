import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import './game.css'
import { boardArray, checkPlacementValid, getCoordinates, getCards, getInitialValidIndexes, getRotation, getValidIndexes, nameToBinary } from './gameUtils/cardUtils'
import { skills } from './gameUtils/skillsUtils'
import tokenService from '../services/token.service'

const isHost = true
const jwt = tokenService.getLocalAccessToken();

export default function GamePage () {
    const { gameId } = useParams()
    const navigate = useNavigate()
    const [selectedCard, setSelectedCard] = useState(null)
    const [board, setBoard] = useState(boardArray)
    const [lastPlacedCard, setLastPlacedCard] = useState(null)
    const [nextValidIndexes, setNextValidIndexes] = useState(getInitialValidIndexes(isHost))
    const [gameData, setGameData] = useState(null)
    const [elapsed, setElapsed] = useState(0)
    const [currentUser, setCurrentUser] = useState(tokenService.getUser());
    const [gamePlayer, setGamePlayer] = useState(null);
    const [color, setColor] = useState(null);
    const [cards, setCards] = useState([]);
    const [randomCards, setRandomCards] = useState([]);

    if (gameData != null && gameData.startedAt == null) {
        navigate(`/lobby/${gameId}`)
    }

    const handleCardSelect = (card) => {
        setSelectedCard(prevCard => (prevCard === card ? null : card))
    }

    const handlePlaceCard = (index) => {
        if (lastPlacedCard == null && !getInitialValidIndexes(isHost).includes(index)) {
            return
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
            })
            .catch((message) => alert(message))

        if (gameData?.startedAt == null) {
            return
        }

        fetch(
            `/api/v1/gameplayers/${gameId}/${currentUser.id}`,
            {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((res) => res.json())
            .then(data => {
                setGamePlayer(data)
                setColor(data.color)
            })
            .catch((message) => alert(message))


        const startedAt = new Date(gameData.startedAt).getTime()

        const interval = setInterval(() => {
            setElapsed(Date.now() - startedAt)
        }, 1000)

        return () => {
            clearInterval(interval)
            abortController.abort('Component unmounted')
        }
    }, [gameData?.startedAt, gameId, currentUser.id, jwt])

    useEffect(() => {
        if (color != null && cards.length === 0) {
            fetch(
            `/api/v1/cards/color/${color}`,
            {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((res) => res.json())
            .then(data => {
                setCards(data)
                console.log(data)
            })
            .catch((message) => alert(message))
        }

        setRandomCards(getCards(cards));
    }, [color, cards]);

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
                        randomCards.map((card) => {
                            const cardName = card.image.split('/').pop().replace('.png', '');
                            const bits = nameToBinary(cardName)

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
                                    <img 
                                        src={card.image} 
                                        alt={`Card ${cardName}`} 
                                        className='card-image'
                                    />
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

                            return (
                                <button
                                    key={index}
                                    // className='game-cell'
                                    className={`
                                        game-cell
                                        ${nextValidIndexes.includes(index) && 'pulsating-bg'}    
                                    `}
                                    onClick={() => handlePlaceCard(index)}
                                    style={{
                                        rotate: card != null ? `${card.rotation * 90}deg` : '0deg',
                                        borderLeft: (bits & 0b1000) ? '5px solid red' : '5px solid transparent',
                                        borderTop: (bits & 0b0100) ? '5px solid red' : '5px solid transparent',
                                        borderRight: (bits & 0b0010) ? '5px solid red' : '5px solid transparent',
                                        borderBottom: card != null ? '5px solid yellow' : '5px solid transparent'
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
