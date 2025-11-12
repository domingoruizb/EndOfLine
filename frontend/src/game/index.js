import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import './game.css'
import { boardArray, checkPlacementValid, getCoordinates, getCards, getInitialValidIndexes, getRotation, getValidIndexes, nameToBinary } from './gameUtils/cardUtils'
import { skills } from './gameUtils/skillsUtils'
import tokenService from '../services/token.service'

const jwt = tokenService.getLocalAccessToken()
const user = tokenService.getUser()

export default function GamePage () {
    const { gameId } = useParams()
    const navigate = useNavigate()
    const [selectedCard, setSelectedCard] = useState(null)
    const [board, setBoard] = useState(boardArray)
    const [lastPlacedCards, setLastPlacedCards] = useState([])
    const [nextValidIndexes, setNextValidIndexes] = useState([])
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

    const isHost = useMemo(() => {
        const hostId = gameData?.host?.id
        const userId = user?.id
        return hostId != null && userId != null ? hostId === userId : false
    }, [gameData?.host?.id])

    const handleCardSelect = (cardName) => {
        setSelectedCard(prevCard => (prevCard === cardName ? null : cardName))
    }

    const handlePlaceCard = (index) => {
        if (selectedCard == null) {
            return
        }

        if (lastPlacedCards.length === 0) {
            if (isHost == null) {
                return
            }

            if (!getInitialValidIndexes(isHost).includes(index)) {
                return
            }
        }

        const lastPlacedCard = lastPlacedCards.length > 0 ? lastPlacedCards[lastPlacedCards.length - 1] : null
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

        setLastPlacedCards(prevCards => [...prevCards, lastPlaced])
        console.log('Placed card:', [...lastPlacedCards, lastPlaced])

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
            .catch((message) => console.log(message))

        return () => abortController.abort('Component unmounted')
    }, [gameData?.startedAt, gameId])

    useEffect(() => {
        const lastPlacedCard = lastPlacedCards.length > 0 ? lastPlacedCards[lastPlacedCards.length - 1] : null
        if (lastPlacedCard == null && isHost != null) {
            const initialIndexes = getInitialValidIndexes(isHost)
            setNextValidIndexes(initialIndexes)
        }
    }, [isHost, lastPlacedCards])
  
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

    function getCardName(card) {
        return card?.image?.split('/')?.pop()?.replace('.png', '');
    }

    useEffect(() => {
        const abortController = new AbortController()
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

        return () => abortController.abort('Component unmounted')
    }, [gameId, currentUser.id, jwt])

    useEffect(() => {
        if (color != null && cards.length === 0) {
            fetch(
            `/api/v1/cards/lineColor/${color}`,
            {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((res) => res.json())
            .then(data => {
                setCards(data)
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
                        skills.map((skill, index) => (
                            <button
                                key={index}
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
                        randomCards.map((card, index) => {
                            const cardName = getCardName(card);
                            const bits = nameToBinary(cardName)
                            const isSelected = selectedCard === cardName;
                            const transformStyle = isSelected ? 'scale(1.05) rotate(2.5deg)' : 'none';

                            return (
                                <button
                                    key={index}
                                    onClick={() => handleCardSelect(cardName)}
                                    className='card-button'
                                        style={{ 
                                            backgroundColor: isSelected && 'var(--main-orange-color)',
                                            transform: transformStyle,
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
                            let cardName = card != null ? card.name : null;
                            const bits = cardName != null ? nameToBinary(cardName) : 0b0000
                            const isValid = nextValidIndexes.includes(index)
                            if (card?.name === 'START' && color != null) {
                                const colorLetter = color.charAt(0).toUpperCase();
                                cardName = `C${colorLetter}_START`;
                            }

                            return (
                                <button
                                    key={index}
                                    className='game-cell'
                                    onClick={() => handlePlaceCard(index)}
                                    style={{
                                        rotate: card != null ? `${card.rotation * 90}deg` : '0deg',
                                        animationName: isValid ? 'pulsate' : 'none',
                                        animationDuration: isValid ? '2s' : undefined,
                                        animationIterationCount: isValid ? 'infinite' : undefined,
                                        animationFillMode: isValid ? 'both' : undefined
                                    }}
                                >
                                    {
                                        card != null ? 
                                        <img 
                                        src={`/cardImages/${cardName}.png`} 
                                        alt={`Card ${cardName}`} 
                                        className='card-image'
                                        /> : (
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
