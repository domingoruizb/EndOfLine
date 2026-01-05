import { useNavigate } from 'react-router-dom'
import tokenService from '../../services/token.service'
import { isTurn } from '../gameUtils/utils'
import '../../static/css/game/gameActions.css'

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
        <div
            className='game-actions-container'
        >
            {
                !game.spectating && (
                    <>
                        {
                            game.deckChangeAvailable && isTurn(game) && (
                                <button
                                    className='action-button'
                                    onClick={requestNewDeck}
                                >
                                    CHANGE DECK
                                </button>
                            )
                        }
                        <button
                            className='action-button'
                            onClick={toggleGiveUpModal}
                        >
                            GIVE UP
                        </button>
                    </>
                )
            }
            <button
                className='action-button'
                onClick={toggleRulesModal}
            >
                RULES
            </button>
            {
                game.spectating && (
                    <>
                        <button
                            className='action-button'
                            onClick={() => navigate(isAdmin ? '/games' : '/friends')}
                        >
                            LEAVE
                        </button>
                        <div
                            className='action-button spectator-badge'
                        >
                            👁️ SPECTATOR MODE
                        </div>
                    </>
                )
            }
        </div>
    )
}
