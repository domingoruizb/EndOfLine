import { useState, useEffect } from 'react'
import { getHost, getTime } from '../gameUtils/utils'
import '../../static/css/game/gameInfo.css'

export default function GameInfo ({
    game
}) {
    const [elapsed, setElapsed] = useState(0)
    const host = getHost(game)

    useEffect(() => {
        if (game?.startedAt == null) {
            return
        }

        const startTime = new Date(game.startedAt).getTime()
        const updateElapsed = () => {
            setElapsed(Date.now() - startTime)
        }

        updateElapsed()

        const interval = setInterval(updateElapsed, 1000)

        return () => clearInterval(interval)
    }, [game?.startedAt])

    const {
        seconds,
        minutes,
        hours
    } = getTime(elapsed)

    return game != null && (
        <div
            className='game-info-container'
        >
            <div>
                Host:
                <span
                    style={{
                        borderColor: game.spectating && game.turn === host.userId ? '#b1d12d' : 'var(--main-orange-color)',
                        backgroundColor: game.spectating && game.turn === host.userId ? 'rgba(177, 209, 45, 0.2)' : 'transparent'
                    }}
                >
                    {host.username}
                </span>
            </div>
            <div>
                Guest:
                <span
                    style={{
                        borderColor: game.spectating && game.turn !== host.userId ? '#b1d12d' : 'var(--main-orange-color)',
                        backgroundColor: game.spectating && game.turn !== host.userId ? 'rgba(177, 209, 45, 0.2)' : 'transparent'
                    }}
                >
                    {
                        game.players
                            .find(player => !player.isHost)
                            ?.username ?? 'Waiting for guest...'
                    }
                </span>
            </div>
            <div>
                Elapsed:
                <span>
                    {hours.toString().padStart(2, '0')}:
                    {minutes.toString().padStart(2, '0')}:
                    {seconds.toString().padStart(2, '0')}
                </span>
            </div>
            <div>
                Round:
                <span>
                    {game.round}
                </span>
            </div>
        </div>
    )
}
