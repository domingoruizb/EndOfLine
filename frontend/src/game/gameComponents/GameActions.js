import { isTurn } from '../gameUtils/utils'

export default function GameActions ({
    game,
    toggleRulesModal,
    toggleGiveUpModal,
    requestNewDeck
}) {
    return game != null && (
        <>
            {
                game.deckChangeAvailable && isTurn(game) && (
                    <button
                        className='change-deck-button'
                        onClick={requestNewDeck}
                    >
                        CHANGE DECK
                    </button>
                )
            }
            <button
                className='giveup-button'
                onClick={toggleRulesModal}
            >
                RULES
            </button>
            <button
                className='giveup-button'
                onClick={toggleGiveUpModal}
            >
                GIVE UP
            </button>
        </>
    )
}
