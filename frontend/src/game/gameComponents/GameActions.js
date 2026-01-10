import { useNavigate } from 'react-router-dom'
import tokenService from '../../services/token.service'
import { isTurn } from '../gameUtils/utils'
import { useFetchResource } from '../../util/useFetchResource'
import '../../static/css/game/gameActions.css'

const user = tokenService.getUser();
const isAdmin = user && user.roles ? user.roles.includes('ADMIN') : false;

export default function GameActions ({
    game,
    toggleRulesModal,
    toggleGiveUpModal
}) {
    const navigate = useNavigate()
    const { getData } = useFetchResource()

    const handleChangeDeck = async () => {
        await getData(
            `/api/v1/board/${game.gameId}/change`,
            'POST'
        )
    }

    return (
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
                                    onClick={handleChangeDeck}
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
                            SPECTATING
                        </div>
                    </>
                )
            }
        </div>
    )
}
