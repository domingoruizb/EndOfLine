import { useNavigate } from 'react-router-dom'
import tokenService from '../../services/token.service'
import { isTurn } from '../gameUtils/utils'

const user = tokenService.getUser();
const isAdmin = user && user.roles ? user.roles.includes('ADMIN') : false;

export default function GameActions ({
    game,
    toggleRulesModal,
    toggleGiveUpModal,
    requestNewDeck
}) {
    const navigate = useNavigate()

    return game != null && (
        <>
            {
                !game.spectating && (
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
                            onClick={toggleGiveUpModal}
                        >
                            GIVE UP
                        </button>
                    </>
                )
            }
            <div
                className='spectator-actions'
            >
                <button
                    className='giveup-button'
                    onClick={toggleRulesModal}
                >
                    RULES
                </button>
                {
                    game.spectating && (
                        <>
                            <button
                                className='giveup-button spectator-leave-button'
                                onClick={() => navigate(isAdmin ? '/games' : '/friends')}
                            >
                                LEAVE
                            </button>
                            <div className='spectator-badge'>
                                👁️ SPECTATOR MODE
                            </div>
                        </>
                    )
                }
            </div>
        </>
    )
}
