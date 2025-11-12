import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import './game.css'
import { checkPlacementValid, getInitialValidIndexes, getRotation, getValidIndexes } from './gameUtils/algorithmUtils'
import { boardArray, getCards } from './gameUtils/cardUtils'
import { skills } from './gameUtils/skillsUtils'
import tokenService from '../services/token.service'
import { postCardPlacement } from './gameUtils/apiUtils'
import GameInfo from './gameComponents/gameInfo'
import SkillButton from './gameComponents/skillButton'
import HandCard from './gameComponents/handCard'

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
    const [hostGamePlayer, setHostGamePlayer] = useState(null);
    const [secondGamePlayer, setSecondGamePlayer] = useState(null);
    const [hostColor, setHostColor] = useState(null);
    const [secondColor, setSecondColor] = useState(null);
    const [hostCards, setHostCards] = useState([]);
    const [secondCards, setSecondCards] = useState([]);
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

    const handlePlaceCard = async (index) => {
        if (!checkPlacementValid(board, selectedCard, index, lastPlacedCards, isHost)) {
            return
        }

        const lastPlacedCard = lastPlacedCards.length > 0 ? lastPlacedCards[lastPlacedCards.length - 1] : null
        const rotation = getRotation(index, lastPlacedCard)

        try {
            const gamePlayerId = isHost ? hostGamePlayer?.id : secondGamePlayer?.id
            await postCardPlacement(index, rotation, selectedCard, gamePlayerId)

            const newBoard = [...board]
            newBoard[index] = { name: selectedCard, rotation }

            console.log('Placing card:', { name: selectedCard, index, rotation })
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
        } catch (err) {
            console.error('Error placing card:', err)
        }
    }

    const fetchGameData = async (signal) => {
        try {
            const res = await fetch(
                `/api/v1/games/${gameId}`,
                {
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                    },
                    signal: signal
                }
            )
            if (!res.ok) throw new Error('Failed to fetch game data');
            
            const data = await res.json()
            setGameData(data)

            const hostPlayer = data?.gamePlayers?.find(player => player.user.id === data.host.id);
            const secondPlayer = data?.gamePlayers?.find(player => player.user.id !== data.host.id);
            
            setHostGamePlayer(hostPlayer);
            setSecondGamePlayer(secondPlayer);

            if (hostColor !== hostPlayer?.color) {
                setHostColor(hostPlayer?.color || null);
            }
            if (secondColor !== secondPlayer?.color) {
                setSecondColor(secondPlayer?.color || null);
            }
            
        } catch (error) {
            if (error.name !== 'AbortError') {
                console.error("Error fetching game data:", error)
            }
        }
    }

    useEffect(() => {
        let intervalId;
        const pollingInterval = 3000;
        let abortController = new AbortController();

        fetchGameData(abortController.signal)

        intervalId = setInterval(() => {
            abortController.abort(); 
            abortController = new AbortController();

            fetchGameData(abortController.signal);
        }, pollingInterval);
        return () => {
            clearInterval(intervalId);
            abortController.abort();
        }
    }, [gameId, jwt, hostColor, secondColor])


    useEffect(() => {
        const lastPlacedCard = lastPlacedCards.length > 0 ? lastPlacedCards[lastPlacedCards.length - 1] : null
        if (lastPlacedCard == null && isHost != null) {
            const initialIndexes = getInitialValidIndexes(isHost)
            setNextValidIndexes(initialIndexes)
        } else if (lastPlacedCard != null) {
             const nextIndexes = getValidIndexes(lastPlacedCard, board)
             setNextValidIndexes(nextIndexes)
        }
    }, [isHost, lastPlacedCards, board])
 
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

    useEffect(() => {
        if (hostColor != null && hostCards.length === 0) {
            fetch(
            `/api/v1/cards/color/${hostColor}`,
            {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((res) => res.json())
            .then(data => {
                setHostCards(data)
            })
            .catch((message) => console.error(message))
        }

        if (secondColor != null && secondCards.length === 0) {
            fetch(
            `/api/v1/cards/color/${secondColor}`,
            {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((res) => res.json())
            .then(data => {
                setSecondCards(data)
            })
            .catch((message) => console.error(message))
        }
    }, [hostColor, secondColor, jwt, hostCards.length, secondCards.length]);

    useEffect(() => {
        if (hostCards.length > 0 && isHost) {
            setRandomCards(getCards(hostCards));
        } else if (secondCards.length > 0 && !isHost) {
            setRandomCards(getCards(secondCards));
        }
    }, [isHost, hostCards, secondCards]);

    return (
        <div
            className='game-page-container'
        >
            <div
                className='game-data-container'
            >
                <GameInfo
                    gameId={gameId}
                    gameData={gameData}
                    elapsed={elapsed}
                />
            </div>
            <div
                className='side-container'
            >
                <div
                    className='skills-container'
                >
                    {
                        skills.map((skill, index) => (
                            <SkillButton
                                key={index}
                                skill={skill}
                            />
                        ))
                    }
                </div>
                <div
                    className='cards-container'
                >
                    {
                        randomCards.map((card, index) => (
                            <HandCard
                                key={index}
                                card={card}
                                selectedCard={selectedCard}
                                handleCardSelect={handleCardSelect}
                            />
                        ))
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

                            const isValid = nextValidIndexes.includes(index)
                            if (card?.name === 'START' && hostColor != null && index === 30) {
                                const colorLetter = hostColor.charAt(0).toUpperCase();
                                cardName = `C${colorLetter}_START`;
                            } else if (card?.name === 'START' && secondColor != null && index === 32) {
                                const colorLetter = secondColor.charAt(0).toUpperCase();
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
                                        card != null ? ( 
                                            <img 
                                                src={`/cardImages/${cardName}.png`} 
                                                alt={`Card ${cardName}`} 
                                                className='card-image'
                                            />
                                        ) : (
                                            <span
                                                style={{
                                                    color: 'gray',
                                                }}
                                            >
                                                .                                                
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