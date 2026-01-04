import { getCardName } from '../gameUtils/utils'

export default function DeckCard ({
    card,
    selected,
    setSelected,
    isCentered
}) {
    const isSelected = selected?.id === card.id

    const name = getCardName(card)

    const centeredStyle = isCentered ? 'centered-last' : ''
    const className = `card-button ${isSelected ? 'selected' : ''} ${centeredStyle}`

    const handleCardSelect = () => {
        setSelected(prev => (prev?.id === card.id ? null : card))
    }

    return (
        <button
            onClick={handleCardSelect}
            className={className}
            style={{
                border: isSelected ? '3px dashed var(--main-orange-color)' : '',
                transform: isSelected ? 'scale(1.05)' : 'none',
            }}
        >
            <img
                src={card.image}
                alt={`Card ${name}`}
                className='card-image'
            />
        </button>
    )
}
