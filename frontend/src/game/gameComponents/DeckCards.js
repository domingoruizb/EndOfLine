import DeckCard from './DeckCard'

export default function DeckCards ({
    game,
    selected,
    setSelected
}) {
    return game != null && (
        <div
            className='cards-container'
        >

            {
                !game.spectating && (
                    game.deck.map((card, index) => (
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
