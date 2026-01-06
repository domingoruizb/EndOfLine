import { useNavigate } from 'react-router-dom'
import tokenService from '../../services/token.service'
import WinnerModal from './WinnerModal'
import LoserModal from './LoserModal'
import GiveUpModal from './GiveUpModal'
import RulesModal from './RulesModal'
import SpectatorModal from './SpectatorModal'
import { giveUp } from '../gameUtils/api'

const user = tokenService.getUser()
const isAdmin = user && user.roles ? user.roles.includes('ADMIN') : false

export default function Modals ({
    game,
    giveUpOpen,
    toggleGiveUpModal,
    rulesOpen,
    toggleRulesModal
}) {
    const navigate = useNavigate()

    const handleGiveUp = async () => {
        toggleGiveUpModal()
        await giveUp(game, navigate)
    }

    return (
        <>
            <WinnerModal
                isOpen={game.endedAt != null && game.winner?.userId === game.userId}
                onConfirm={() => navigate('/creategame')}
            />
            <LoserModal
                isOpen={game.endedAt != null && game.winner?.userId !== game.userId}
                onConfirm={() => navigate('/creategame')}
            />
            <GiveUpModal
                isOpen={giveUpOpen}
                toggle={toggleGiveUpModal}
                onConfirm={handleGiveUp}
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
                winner={game.winner}
            />
        </>
    )
}
