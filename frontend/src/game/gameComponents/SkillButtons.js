import SkillButton from './SkillButton'

const skills = [
    'Speed Up',
    'Brake',
    'Extra Gas',
    'Reverse'
]

export default function SkillButtons ({
    game,
    requestMoreCards
}) {
    return game != null && (
        <div
            className='skills-container'
        >
            {
                skills.map((skill, index) => {
                    return (
                        <SkillButton
                            key={index}
                            skill={skill}
                            game={game}
                            requestMoreCards={requestMoreCards}
                        />
                    )
                })
            }
        </div>
    )
}
