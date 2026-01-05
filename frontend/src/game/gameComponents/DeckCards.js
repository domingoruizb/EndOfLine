import DeckCard from './DeckCard'

export default function DeckCards ({
    game,
    deck,
    selected,
    setSelected
}) {
    return game != null && (
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
                        />
                    ))
                )
            }
        </div>
    )
}
