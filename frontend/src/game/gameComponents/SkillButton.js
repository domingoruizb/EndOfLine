import { setUpSkill } from '../gameUtils/api'

export default function SkillButton ({
    skill,
    game,
    requestMoreCards
}) {
    const enabled = skill === 'Reverse' ? (
        game.skillsAvailable && game.reversible?.length > 0
    ) : (
        game.skillsAvailable && game.placeable?.length > 0
    )

    const formattedSkill = skill.toUpperCase().replace(' ', '_')

    const handleClick = async () => {
        if (enabled) {
            await setUpSkill(formattedSkill, game.gameId, game.userId)

            if (formattedSkill === 'EXTRA_GAS') {
                await requestMoreCards()
            }
        }
    }

    const buttonSkillName = skill.toUpperCase().replace(' ', '_')
    const isActive = game.skill === buttonSkillName

    const buttonClasses = `skill-button
        ${formattedSkill === 'REVERSE' ? 'reverse-button' : ''}
        ${!enabled ? 'skill-button-disabled' : ''}
        ${isActive ? (formattedSkill === 'REVERSE' ? 'reverse-button-active' : 'skill-button-active') : ''}
    `

    return (
        <button
            className={buttonClasses}
            onClick={handleClick}
            disabled={!enabled}
        >
            {skill}
        </button>
    )
}
