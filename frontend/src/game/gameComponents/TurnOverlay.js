import { isTurn } from '../gameUtils/utils'
import '../../static/css/game/gameOverlay.css'

export default function TurnOverlay ({
    game
}) {
    return !isTurn(game) && game.endedAt == null && !game.spectating && (
        <div
            className='turn-overlay'
        >
            <span
                className='overlay-text'
            >
                ⏳ Waiting for opponent's turn...
            </span>
        </div>
    )
}
