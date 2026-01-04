import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import tokenService from '../services/token.service'
import { useBoard } from './gameHooks/useBoard'
import { useDeck } from './gameHooks/useDeck'
import { useGame } from './gameHooks/useGame'
import { placeCard } from './gameUtils/api'
import { getEnergyRotation, getOwnColor } from './gameUtils/utils'
import GameInfo from './gameComponents/GameInfo'
import DeckCards from './gameComponents/DeckCards'
import BoardCell from './gameComponents/BoardCell'
import TurnOverlay from './gameComponents/TurnOverlay'
import SkillButtons from './gameComponents/SkillButtons'
import GameChat from './gameComponents/GameChat'
import Modals from './gameComponents/Modals'
import GameActions from './gameComponents/GameActions'
import '../static/css/game/game.css'

const jwt = tokenService.getLocalAccessToken()

export default function GamePage () {
    const navigate = useNavigate()
    const { gameId } = useParams()
    const { game } = useGame(gameId)
    const { board } = useBoard(game)
    const { deck, removeCard, requestNewDeck, requestMoreCards } = useDeck(gameId, game?.round)
    const [selected, setSelected] = useState(null)
    const [rulesOpen, setRulesOpen] = useState(false)
    const [giveUpOpen, setGiveUpOpen] = useState(false)

    const toggleRulesModal = () => setRulesOpen(!rulesOpen)
    const toggleGiveUpModal = () => setGiveUpOpen(!giveUpOpen)

    if (game != null && game.startedAt == null) {
        navigate(`/lobby/${gameId}`)
    }

    const handlePlaceCard = async (index) => {
        try {
            if (selected == null) {
                return
            }

            if (!game.placeable.includes(index)) {
                return
            }

            // Place the card via API
            await placeCard(gameId, selected, index)

            // Remove placed card from deck
            removeCard(selected.id)
            setSelected(null)
        } catch (err) {
            console.error('Error placing card:', err)
        }
    }

    return (
        <div
            className='game-page-container'
        >
            <Modals
                game={game}
                giveUpOpen={giveUpOpen}
                toggleGiveUpModal={toggleGiveUpModal}
                rulesOpen={rulesOpen}
                toggleRulesModal={toggleRulesModal}
            />
            <div
                className='floating-game-actions'
            >
                <GameActions
                    game={game}
                    toggleRulesModal={toggleRulesModal}
                    toggleGiveUpModal={toggleGiveUpModal}
                    requestNewDeck={requestNewDeck}
                />
            </div>
            <div
                className='game-data-container'
            >
                {
                    game != null && (
                        <GameInfo
                            game={game}
                            isSpectator={false}
                        />
                    )
                }
            </div>
            <div
                className='side-container'
            >
                <SkillButtons
                    game={game}
                    requestMoreCards={requestMoreCards}
                />
                <DeckCards
                    game={game}
                    deck={deck}
                    selected={selected}
                    setSelected={setSelected}
                />
            </div>
            <div
                className='game-board-container'
            >
                <div
                    className='game-board'
                >
                    {
                        board.map((card, index) => (
                            <BoardCell
                                key={index}
                                game={game}
                                card={card}
                                index={index}
                                handlePlaceCard={handlePlaceCard}
                            />
                        ))
                    }
                </div>
                <TurnOverlay
                    game={game}
                />
            </div>
            <div
                className='side-container'
            >
                {
                    game != null && (
                        <>
                            <img
                                src={`/cardImages/C${getOwnColor(game)}_ENERGY.png`}
                                alt='Energy Symbol'
                                className='card-button'
                                style={{
                                    rotate: getEnergyRotation(game) + 'deg'
                                }}
                            />
                            <GameChat game={game} jwt={jwt} />
                        </>
                    )
                }
            </div>
        </div>
    )
}