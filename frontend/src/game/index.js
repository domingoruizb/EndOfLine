import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useBoard } from './gameHooks/useBoard'
import { useGame } from './gameHooks/useGame'
import GameInfo from './gameComponents/GameInfo'
import DeckCards from './gameComponents/DeckCards'
import BoardCell from './gameComponents/BoardCell'
import TurnOverlay from './gameComponents/TurnOverlay'
import SkillButtons from './gameComponents/SkillButtons'
import GameChat from './gameComponents/GameChat'
import EnergyCard from './gameComponents/EnergyCard'
import ActionsModals from './gameComponents/ActionsModals'
import '../static/css/game/game.css'

export default function GamePage () {
    const navigate = useNavigate()
    const { gameId } = useParams()
    const { game } = useGame(gameId)
    const { board } = useBoard(game)
    const [selected, setSelected] = useState(null)

    if (game != null && game.startedAt == null) {
        navigate(`/lobby/${gameId}`)
    }

    return game != null && (
        <div
            className='game-page-container'
        >
            <GameInfo
                game={game}
            />
            <ActionsModals
                game={game}
            />
            {
                !game.spectating && (
                    <div
                        className='side-container'
                    >
                        <SkillButtons
                            game={game}
                        />
                        <DeckCards
                            game={game}
                            selected={selected}
                            setSelected={setSelected}
                        />
                    </div>
                )
            }
            <div
                className='game-board-container'
            >
                <div
                    className='game-board'
                >
                    {
                        board.map((cell, index) => (
                            <BoardCell
                                key={index}
                                game={game}
                                cell={cell}
                                index={index}
                                selected={selected}
                                setSelected={setSelected}
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
                <EnergyCard
                    game={game}
                />
                <GameChat
                    game={game}
                />
            </div>
        </div>
    )
}