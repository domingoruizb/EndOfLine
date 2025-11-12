import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import './game.css'
import { boardArray, checkPlacementValid, getCards, getInitialValidIndexes, getRotation, getValidIndexes, nameToBinary } from './gameUtils/cardUtils'
import { skills } from './gameUtils/skillsUtils'
import tokenService from '../services/token.service'
import GiveUpModal from './gameUtils/giveUpModal'

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
    const [isGiveUpModalOpen, setIsGiveUpModalOpen] = useState(false);

    const toggleGiveUpModal = () => setIsGiveUpModalOpen(!isGiveUpModalOpen);

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

const handleGiveUp = async () => {
        toggleGiveUpModal();

        try {
            const res = await fetch(
                `/api/v1/games/${gameId}/${user.id}/giveup`,
                {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${jwt}`,
                    },
                }
            );

            if (!res.ok) {
                const errorText = await res.text();
                try {
                    const errorData = JSON.parse(errorText);
                    throw new Error(errorData.message || 'Failed to give up the game');
                } catch (e) {
                    throw new Error(`Failed to give up the game: ${res.status} ${res.statusText}`);
                }
            }

            navigate('/creategame'); 

        } catch (error) {
            console.error("Error giving up the game:", error);
            alert(`Error giving up: ${error.message}`);
        }
    };  

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

            if (gameData.endedAt != null) {
                navigate(`/creategame`);
            }

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

        fetchGameData(abortController.signal);

        intervalId = setInterval(() => {
            abortController.abort(); 
            abortController = new AbortController();

            fetchGameData(abortController.signal);
        }, pollingInterval);
        return () => {
            clearInterval(intervalId);
            abortController.abort();
        }
    }, [jwt, hostColor, secondColor, gameData])


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

    function getCardName(card) {
        return card?.image?.split('/')?.pop()?.replace('.png', '');
    }

    useEffect(() => {
        if (hostColor != null && hostCards.length === 0) {
            fetch(
            `/api/v1/cards/lineColor/${hostColor}`,
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
            `/api/v1/cards/lineColor/${secondColor}`,
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

    const seconds = Math.floor(elapsed / 1000) % 60
    const minutes = Math.floor(elapsed / 60000) % 60
    const hours = Math.floor(elapsed / 3600000)

    const isGameActive = gameData?.startedAt != null && gameData?.endedAt == null;

    return (
        <div
            className='game-page-container'
        >
            <GiveUpModal 
                isOpen={isGiveUpModalOpen}
                toggle={toggleGiveUpModal}
                onConfirm={handleGiveUp} 
            />
            {isGameActive && (
                <div className='floating-game-actions'>
                    <button
                        className='giveup-button'
                        onClick={toggleGiveUpModal}
                    >
                        Give up
                    </button>
                </div>
            )}
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