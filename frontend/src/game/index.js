import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import jwt_decode from 'jwt-decode'
import './game.css'
import { canReverse, checkPlacementValid, getInitialValidIndexes, getReverseCard, getRotation, getValidIndexes } from './gameUtils/algorithmUtils'
import { boardArray, calculateRotation, getCardColor, getCards, getIndex } from './gameUtils/cardUtils'
import { skills } from './gameUtils/skillsUtils'
import tokenService from '../services/token.service'
import { handleGiveUp, loseAndEndGame, postCardPlacement } from './gameUtils/apiUtils'
import GameInfo from './gameComponents/gameInfo'
import SkillButton from './gameComponents/skillButton'
import HandCard from './gameComponents/handCard'
import GiveUpModal from './gameComponents/giveUpModal'
import WinnerModal from './gameComponents/winnerModal'
import LoserModal from './gameComponents/loserModal'
import SpectatorEndModal from './gameComponents/spectatorEndModal'
import GameChat from './gameComponents/gameChat'
import RulesModal from './gameComponents/rulesModal'

const jwt = tokenService.getLocalAccessToken()
const user = tokenService.getUser()
const isAdmin = user.roles.includes('ADMIN')

export default function GamePage () {
    const { gameId } = useParams()
    const navigate = useNavigate()
    const [selectedCard, setSelectedCard] = useState(null)
    const [board, setBoard] = useState(boardArray)
    const [lastPlacedCards, setLastPlacedCards] = useState([])
    const [lastPlacedCard, setLastPlacedCard] = useState(null)
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
    const [isRulesModalOpen, setIsRulesModalOpen] = useState(false);
    const [hasLost, setHasLost] = useState(false);
    const [cardsPlacedInTurn, setCardsPlacedInTurn] = useState(0);
    const [changeDeckUsed, setChangeDeckUsed] = useState(false);

    const toggleGiveUpModal = () => setIsGiveUpModalOpen(!isGiveUpModalOpen);
    const toggleRulesModal = () => setIsRulesModalOpen(!isRulesModalOpen);

    if (gameData != null && gameData.startedAt == null) {
        navigate(`/lobby/${gameId}`)
    }

    const handleChangeDeck = () => {
        setChangeDeckUsed(true); 
        const allCards = isHost ? hostCards : secondCards;
        const newRandomCards = getCards(allCards); 
        setRandomCards(newRandomCards); 
    }

    const getCardName = (card) => {
        return card?.image?.split('/')?.pop()?.replace('.png', '');
    }

    const isHost = useMemo(() => {
        const hostId = gameData?.host?.id
        const userId = user?.id
        return hostId != null && userId != null ? hostId === userId : false
    }, [gameData?.host?.id])

    const isSpectator = useMemo(() => {
        const userId = user?.id
        if (!gameData || !gameData.gamePlayers || userId == null) return false
        return !gameData.gamePlayers.some(gp => gp.user.id === userId)
    }, [gameData, user?.id])

    const handleCardSelect = (cardName) => {
        setSelectedCard(prevCard => (prevCard === cardName ? null : cardName))
    }

    const handlePlaceCard = async (index) => {
        if (isSpectator) return;
        
        if (!checkPlacementValid(board, selectedCard, index, lastPlacedCards, isHost, lastPlacedCard)) {
            return
        }

        const rotation = getRotation(index, lastPlacedCard)

        const isTurnFinished = cardsPlacedInTurn + 1 === cardsPerTurnLimit;

        const cardUsed = selectedCard;

        try {
            const gamePlayerId = isHost ? hostGamePlayer?.id : secondGamePlayer?.id
            await postCardPlacement(index, rotation, selectedCard, gamePlayerId, isTurnFinished)

            const newBoard = board.map((cell, i) => i === index ? { name: selectedCard, rotation } : cell);
            setBoard(newBoard)

            const lastPlaced = {
                name: selectedCard,
                index,
                rotation
            }

            setLastPlacedCard(lastPlaced)
            setLastPlacedCards(prevCards => [...prevCards, lastPlaced])

            const nextIndexes = getValidIndexes(lastPlaced, newBoard)
            setNextValidIndexes(nextIndexes)

            if (nextIndexes.length === 0) {
                setHasLost(true);
            }

            setRandomCards(prevRandomCards => {
                const cardIndexToReplace = prevRandomCards.findIndex(card => getCardName(card) === cardUsed);
                
                if (cardIndexToReplace !== -1) {
                    const updatedCards = [...prevRandomCards];
                    updatedCards.splice(cardIndexToReplace, 1); 
                    return updatedCards;
                }
                return prevRandomCards;
            });

            if (isTurnFinished) {
                setCardsPlacedInTurn(0);
                replaceHandCard();
            } else {
                setCardsPlacedInTurn(prev => prev + 1);
            }

            setSelectedCard(null)
        } catch (err) {
            console.error('Error placing card:', err)
        }
    }

    const replaceHandCard = () => {
        setRandomCards(prevRandomCards => {
            const currentHandCount = prevRandomCards.length;
            const targetHandCount = 5;
            const cardsToDraw = targetHandCount - currentHandCount;
            
            if (cardsToDraw <= 0) {
                return prevRandomCards; 
            }
            
            const allCards = isHost ? hostCards : secondCards;
            const cardNamesInHand = prevRandomCards.map(c => getCardName(c));
            
            const availableCards = allCards.filter(card => !cardNamesInHand.includes(getCardName(card)));
            
            const updatedCards = [...prevRandomCards];

            for (let i = 0; i < cardsToDraw; i++) {
                if (availableCards.length > 0) {
                    const randomIndex = Math.floor(Math.random() * availableCards.length);
                    const newCard = availableCards.splice(randomIndex, 1)[0];
                    updatedCards.push(newCard);
                } else {
                    break;
                }
            }

            return updatedCards;
        });
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

    const fetchCards = async (signal) => {
        try {
            const cards = await fetch(
                `/api/v1/cards/game/${gameId}`,
                {
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                    },
                    signal: signal
                }
            )

            if (!cards.ok) throw new Error('Failed to fetch cards');
            const cardsData = await cards.json()

            setBoard(prevBoard => {
                const newBoard = [...prevBoard]
                cardsData.forEach(cgp => {
                    const index = getIndex(cgp.positionX, cgp.positionY)
                    newBoard[index] = {
                        name: cgp.card.image.replace('/cardImages/', '').replace('.png', ''),
                        rotation: cgp.rotation
                    }
                })

                return newBoard
            })
        } catch (err) {
            if (err.name !== 'AbortError') {
                console.error('Error fetching cards:', err)
            }
        }
    }

    useEffect(() => {
        let intervalId;
        const pollingInterval = 100;
        let abortController = new AbortController();

        fetchGameData(abortController.signal)
        fetchCards(abortController.signal)

        intervalId = setInterval(() => {
            abortController.abort();
            abortController = new AbortController();

            fetchGameData(abortController.signal);
            fetchCards(abortController.signal);
        }, pollingInterval);
        return () => {
            clearInterval(intervalId);
            abortController.abort();
        }
    }, [jwt, hostColor, secondColor])

    useEffect(() => {
        if (hasLost) {
            loseAndEndGame(gameId)
        }
    }, [hasLost]);

    useEffect(() => {
        if (gameData != null && gameData?.skill === 'EXTRA_GAS' && gameData?.turn === user?.id) {
            setRandomCards(prevCards => {
                if (prevCards.length < 6) {
                    const allCards = isHost ? hostCards : secondCards;
                    const cardNamesInHand = prevCards.map(c => getCardName(c));
                    const availableCards = allCards.filter(card => !cardNamesInHand.includes(getCardName(card)));
                    const randomIndex = Math.floor(Math.random() * availableCards.length);
                    const newCard = availableCards[randomIndex];
                    return [...prevCards, newCard];
                }
                return prevCards;
            });
        }

        if (gameData != null && gameData?.skill === 'REVERSE' && gameData?.turn === user?.id) {
            const reverseCard = getReverseCard([...lastPlacedCards, lastPlacedCard], lastPlacedCard);
            const validInd = reverseCard ? getValidIndexes(reverseCard, board) : [];

            setLastPlacedCard(reverseCard);
            setNextValidIndexes(validInd);
        }
    }, [gameData?.skill]);

    useEffect(() => {

        if (lastPlacedCard == null && isHost != null) {
            const initialIndexes = getInitialValidIndexes(isHost)
            setNextValidIndexes(initialIndexes)
        } else if (lastPlacedCard != null) {
            const nextIndexes = getValidIndexes(lastPlacedCard, board)
            if (nextIndexes.length === 0) {
                setHasLost(true);
            }
            setNextValidIndexes(nextIndexes)
        }
    }, [isHost, lastPlacedCard, board])
 
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

    const isGameActive = gameData?.startedAt != null && gameData?.endedAt == null;

    const isMyTurn = !isSpectator && isGameActive && gameData?.turn === user?.id;

    const currentRound = gameData?.round;

    let calculatedCardsPerTurnLimit;
    if (gameData?.skill != null) {
        if (gameData.skill === 'SPEED_UP') {
            calculatedCardsPerTurnLimit = 3;
        } else if (gameData.skill === 'BRAKE') {
            calculatedCardsPerTurnLimit = 1;
        } else {
            calculatedCardsPerTurnLimit = currentRound === 1 ? 1 : 2;
        }
    } else {
        calculatedCardsPerTurnLimit = currentRound === 1 ? 1 : 2;
    }

    const cardsPerTurnLimit = calculatedCardsPerTurnLimit;
    const hasReachedLimit = cardsPlacedInTurn >= cardsPerTurnLimit;
    const rotation = calculateRotation(isHost, hostGamePlayer, secondGamePlayer);

    const isSkillSelectionDisabled = 
    currentRound === 1 ||           
    gameData?.skill != null ||       
    cardsPlacedInTurn > 0;

    const showChangeDeckButton = isGameActive && currentRound === 1 && isMyTurn;
    const isChangeDeckDisabled = changeDeckUsed;

    return (
        <div
            className='game-page-container'
        >
            <GiveUpModal 
                isOpen={isGiveUpModalOpen}
                toggle={toggleGiveUpModal}
                onConfirm={() => handleGiveUp(
                    gameId,
                    toggleGiveUpModal,
                    navigate
                )}
            />
            <RulesModal
                isOpen={isRulesModalOpen}
                toggle={toggleRulesModal}
            />
            <WinnerModal 
                isOpen={gameData?.endedAt != null && gameData.winner?.id === user?.id}
                onConfirm={() => navigate('/creategame')} 
            />
            <LoserModal 
                isOpen={hasLost}
                onConfirm={
                     () => {
                        navigate('/creategame');
                    }
                } 
            />
            <SpectatorEndModal 
                isOpen={isSpectator && gameData?.endedAt != null}
                toggle={() => navigate(isAdmin ? '/games' : '/friends')}
                onCancel={() => navigate(isAdmin ? '/games' : '/friends')}
                onConfirm={() => navigate('/')}
                winnerUsername={gameData?.winner?.username}
            />
            {isGameActive && !isSpectator && (
                <div className='floating-game-actions'>
                    {showChangeDeckButton && (
                        <button
                            className={`change-deck-button ${isChangeDeckDisabled ? 'change-deck-disabled' : ''}`}
                            onClick={handleChangeDeck}
                            disabled={isChangeDeckDisabled}
                        >
                            CHANGE DECK
                        </button>
                    )}
                    <button
                        className='giveup-button'
                        onClick={toggleRulesModal}
                    >
                        RULES
                    </button>
                    <button
                        className='giveup-button'
                        onClick={toggleGiveUpModal}
                    >
                        GIVE UP
                    </button>
                </div>
            )}
            {isSpectator && isGameActive && (
                <div className='floating-game-actions spectator-actions'>
                    <button
                        className='giveup-button spectator-leave-button'
                        onClick={() => navigate(isAdmin ? '/games' : '/friends')}
                    >
                        Leave
                    </button>
                    <button
                        className='giveup-button'
                        onClick={toggleRulesModal}
                        style={{ marginLeft: '8px' }}
                    >
                        Rules
                    </button>
                    <div className='spectator-badge'>
                        👁️ SPECTATOR MODE
                    </div>
                </div>
            )}
            <div
                        className='game-data-container'
                    >
                        <GameInfo
                            gameId={gameId}
                            gameData={gameData}
                            elapsed={elapsed}
                            isSpectator={isSpectator}
                            user={user}
                        />
                    </div>
            <div
                className='side-container'
            >
                <div
                    className='skills-container'
                >
                    {
                        skills.map((skill, index) => {
                            return (
                                <SkillButton
                                    key={index}
                                    skill={skill}
                                    gameId={gameId}
                                    userId={user.id}
                                    isDisabled={
                                        isSpectator ||
                                        (skill !== 'Reverse' && isSkillSelectionDisabled) ||
                                        (skill === 'Reverse' && (isSkillSelectionDisabled || !canReverse(lastPlacedCards, lastPlacedCard, board)))
                                    }
                                    activeSkill={gameData?.skill}
                                />
                            )
                        })
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
                                handleCardSelect={isMyTurn && !hasReachedLimit ? handleCardSelect : () => {}}
                                isCentered={randomCards.length === 5 && index === 4}
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

                            const isValid = !isSpectator && nextValidIndexes.includes(index)
                            if (card?.name === 'START' && hostColor != null && index === 30) {
                                const colorLetter = getCardColor(true, hostColor, secondColor);
                                cardName = `C${colorLetter}_START`;
                            } else if (card?.name === 'START' && secondColor != null && index === 32) {
                                const colorLetter = getCardColor(false, hostColor, secondColor);
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
                {!isMyTurn && !isSpectator && isGameActive && (
                    <div className='turn-overlay'>
                        <span className='overlay-text'>
                            ⏳ Waiting for opponent's turn...
                        </span>
                    </div>
                )}
            </div>
            <div
                className='side-container'
            >
                <img
                    src={`/cardImages/C${getCardColor(isHost, hostColor, secondColor)}_ENERGY.png`}
                    alt='Energy Symbol'
                    className='card-button'
                    style={{
                        rotate: rotation + 'deg'
                    }}
                />
                <GameChat gameId={gameId} jwt={jwt} user={user} />
            </div>
        </div>
    )
}