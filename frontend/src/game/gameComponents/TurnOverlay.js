import { isTurn } from '../gameUtils/utils'

export default function TurnOverlay ({
    game
}) {
    return game != null && !isTurn(game) && game.endedAt == null && (
        <div className='turn-overlay'>
            <span className='overlay-text'>
                ⏳ Waiting for opponent's turn...
            </span>
        </div>
    )
}
