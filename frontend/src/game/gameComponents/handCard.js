import { getCardName } from '../gameUtils/cardUtils'

export default function HandCard ({
    card,
    selectedCard,
    handleCardSelect,
}) {
    const cardName = getCardName(card)
    const isSelected = selectedCard === cardName
    const transformStyle = isSelected ? 'scale(1.05) rotate(2.5deg)' : 'none'

    return (
        <button
            onClick={() => handleCardSelect(cardName)}
            className='card-button'
            style={{
                backgroundColor: isSelected && 'var(--main-orange-color)',
                transform: transformStyle,
            }}
        >
            <img
                src={card.image}
                alt={`Card ${cardName}`}
                className='card-image'
            />
        </button>
    )
}
