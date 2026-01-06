import { placeCard } from '../gameUtils/api'
import { isTurn } from '../gameUtils/utils'

export default function BoardCell ({
    game,
    cell,
    index,
    selected,
    setSelected
}) {
    const isPlayerTurn = isTurn(game)
    const isPlaceable = isPlayerTurn && game.placeable.includes(index)
    const isReversible = isPlayerTurn && game.reversible.includes(index)

    const handlePlaceCard = async (index) => {
        try {
            if (selected == null) {
                return
            }

            if (!game.placeable.includes(index)) {
                return
            }

            await placeCard(game, selected, index)
            setSelected(null)
        } catch (err) {
            console.error('Error placing card:', err)
        }
    }

    return (
        <button
            className='game-cell'
            onClick={() => handlePlaceCard(index)}
            disabled={!isPlayerTurn}
            style={{
                rotate: cell == null ? '0deg' : `${cell.rotation * 90}deg`,
                border: isPlaceable ? '2px dashed var(--main-orange-color)' : (isReversible && game.skillsAvailable ? '2px dashed #00BCD4' : '2px solid transparent')
            }}
        >
            {
                cell == null ? (
                    <span
                        style={{
                            color: 'gray'
                        }}
                    >
                        .
                    </span>
                ) : (
                    <img
                        src={cell.card.image}
                        alt={`Card ${cell.card.name}`}
                        className='card-image'
                    />
                )
            }
        </button>
    )
}
