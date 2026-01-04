import DeckCard from './DeckCard'

export default function DeckCards ({
    game,
    deck,
    selected,
    setSelected
}) {
    return (
        <div
            className='cards-container'
        >

            {
                deck != null && !game.spectating && (
                    deck.map((card, index) => (
                        <DeckCard
                            key={index}
                            card={card}
                            selected={selected}
                            setSelected={setSelected}
                            isCentered={deck.length === 5 && index === 4}
                        />
                    ))
                )
            }
        </div>
    )
}
