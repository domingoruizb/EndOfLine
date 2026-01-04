import { useNavigate } from 'react-router-dom'
import WinnerModal from './WinnerModal'
import LoserModal from './LoserModal'
import GiveUpModal from './GiveUpModal'
import RulesModal from './RulesModal'
import { giveUp } from '../gameUtils/api'

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
        </>
    )
}
