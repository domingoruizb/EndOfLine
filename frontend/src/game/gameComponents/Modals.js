import { useNavigate } from 'react-router-dom'
import tokenService from '../../services/token.service'
import WinnerModal from './WinnerModal'
import LoserModal from './LoserModal'
import GiveUpModal from './GiveUpModal'
import RulesModal from './RulesModal'
import SpectatorModal from './SpectatorModal'
import { giveUp } from '../gameUtils/api'

const user = tokenService.getUser();
const isAdmin = user && user.roles ? user.roles.includes('ADMIN') : false;

export default function Modals ({
    game,
    giveUpOpen,
    toggleGiveUpModal,
    rulesOpen,
    toggleRulesModal
}) {
    const navigate = useNavigate()

    return game != null && (
        <>
            <WinnerModal
                isOpen={game.endedAt != null && game.winnerId === game.userId}
                onConfirm={() => navigate('/creategame')}
            />
            <LoserModal
                isOpen={game.endedAt != null && game.winnerId !== game.userId}
                onConfirm={() => navigate('/creategame')}
            />
            <GiveUpModal
                isOpen={giveUpOpen}
                toggle={toggleGiveUpModal}
                onConfirm={() => giveUp(
                    game.gameId,
                    toggleGiveUpModal,
                    navigate
                )}
            />
            <RulesModal
                isOpen={rulesOpen}
                toggle={toggleRulesModal}
            />
            <SpectatorModal
                isOpen={game.spectating && game.endedAt != null}
                toggle={() => navigate(isAdmin ? '/games' : '/friends')}
                onCancel={() => navigate(isAdmin ? '/games' : '/friends')}
                onConfirm={() => navigate('/')}
                winnerUsername={game.players?.find(p => p.userId === game.winnerId)?.username}
            />
        </>
    )
}
