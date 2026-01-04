import { useNavigate } from 'react-router-dom'
import LoserModal from './LoserModal'
import WinnerModal from './WinnerModal'

export default function WinLoseModal ({
    game
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
        </>
    )
}
