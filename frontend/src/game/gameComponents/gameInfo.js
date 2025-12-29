export default function GameInfo ({
    gameId,
    gameData,
    elapsed,
    isSpectator,
    user
}) {
    const seconds = Math.floor(elapsed / 1000) % 60
    const minutes = Math.floor(elapsed / 60000) % 60
    const hours = Math.floor(elapsed / 3600000)

    return (
        <>
            {
                gameData != null && gameData.host != null && (
                    <>
                        Host:
                        <span style={{
                            borderColor: isSpectator && gameData.turn === gameData.host.id ? '#b1d12d' : 'var(--main-orange-color)',
                            borderWidth: '2px',
                            borderStyle: 'solid',
                            borderRadius: '10px',
                            padding: '5px 10px',
                            backgroundColor: isSpectator && gameData.turn === gameData.host.id ? 'rgba(177, 209, 45, 0.2)' : 'transparent'
                        }}>
                            {gameData.host.username}
                        </span>
                        Guest:
                        <span style={{
                            borderColor: isSpectator && gameData.turn !== gameData.host.id ? '#b1d12d' : 'var(--main-orange-color)',
                            borderWidth: '2px',
                            borderStyle: 'solid',
                            borderRadius: '10px',
                            padding: '5px 10px',
                            backgroundColor: isSpectator && gameData.turn !== gameData.host.id ? 'rgba(177, 209, 45, 0.2)' : 'transparent'
                        }}>
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
        </>
    )
}
