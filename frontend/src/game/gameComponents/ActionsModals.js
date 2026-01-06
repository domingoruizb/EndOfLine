import { useState } from 'react'
import Modals from './Modals'
import GameActions from './GameActions'

export default function ActionsModals ({
    game
}) {
    const [rulesOpen, setRulesOpen] = useState(false)
    const [giveUpOpen, setGiveUpOpen] = useState(false)

    const toggleRulesModal = () => {
        setRulesOpen(!rulesOpen)
    }

    const toggleGiveUpModal = () => {
        setGiveUpOpen(!giveUpOpen)
    }

    return (
        <>
            <Modals
                game={game}
                giveUpOpen={giveUpOpen}
                toggleGiveUpModal={toggleGiveUpModal}
                rulesOpen={rulesOpen}
                toggleRulesModal={toggleRulesModal}
            />
            <GameActions
                game={game}
                toggleRulesModal={toggleRulesModal}
                toggleGiveUpModal={toggleGiveUpModal}
            />
        </>
    )
}
