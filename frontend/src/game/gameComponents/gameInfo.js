export default function GameInfo ({
    gameId,
    gameData,
    elapsed
}) {
    const seconds = Math.floor(elapsed / 1000) % 60
    const minutes = Math.floor(elapsed / 60000) % 60
    const hours = Math.floor(elapsed / 3600000)

    return (
        <>
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
        </>
    )
}
