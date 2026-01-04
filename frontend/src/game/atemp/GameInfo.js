import { useState, useEffect } from 'react'
import { getHost, getTime } from '../gameUtils/utils'

export default function GameInfo ({
    game,
    isSpectator
}) {
    const [elapsed, setElapsed] = useState(0)
    const host = getHost(game)

    useEffect(() => {
        if (game.startedAt == null) {
            return
        }

        const startTime = new Date(game.startedAt).getTime()
        const updateElapsed = () => {
            setElapsed(Date.now() - startTime)
        }

        updateElapsed()

        const interval = setInterval(updateElapsed, 1000)

        return () => clearInterval(interval)
    }, [game.startedAt])

    const {
        seconds,
        minutes,
        hours
    } = getTime(elapsed)

    return (
        <>
            Host:
            <span style={{
                borderColor: isSpectator && game.turn === host.id ? '#b1d12d' : 'var(--main-orange-color)',
                borderWidth: '2px',
                borderStyle: 'solid',
                borderRadius: '10px',
                padding: '5px 10px',
                backgroundColor: isSpectator && game.turn === host.id ? 'rgba(177, 209, 45, 0.2)' : 'transparent'
            }}>
                {host.username}
            </span>
            Guest:
            <span style={{
                borderColor: isSpectator && game.turn !== host.id ? '#b1d12d' : 'var(--main-orange-color)',
                borderWidth: '2px',
                borderStyle: 'solid',
                borderRadius: '10px',
                padding: '5px 10px',
                backgroundColor: isSpectator && game.turn !== host.id ? 'rgba(177, 209, 45, 0.2)' : 'transparent'
            }}>
                {
                    game.players
                        .find(player => !player.isHost)
                        ?.username ?? 'Waiting for guest...'
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
