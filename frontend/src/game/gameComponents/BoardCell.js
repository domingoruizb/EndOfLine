import { isTurn } from '../gameUtils/utils'

export default function BoardCard ({
    game,
    card,
    index,
    handlePlaceCard
}) {
    const isPlayerTurn = isTurn(game)
    const isPlaceable = isPlayerTurn && game.placeable.includes(index)
    const isReversible = isPlayerTurn && game.reversible.includes(index)

    return (
        <button
            className='game-cell'
            onClick={() => handlePlaceCard(index)}
            disabled={!isPlayerTurn}
            style={{
                rotate: card == null ? '0deg' : `${card.rotation * 90}deg`,
                border: isPlaceable ? '2px dashed var(--main-orange-color)' : (isReversible && game.skillsAvailable ? '2px dashed #00BCD4' : '2px solid transparent')
            }}
        >
            {
                card == null ? (
                    <span
                        style={{
                            color: 'gray'
                        }}
                    >
                        .
                    </span>
                ) : (
                    <img
                        src={card.card.image}
                        alt={`Card ${card.card.name}`}
                        className='card-image'
                    />
                )
            }
        </button>
    )
}
