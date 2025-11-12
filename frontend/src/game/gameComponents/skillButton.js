export default function SkillButton ({
    skill,
}) {
    return (
        <button
            className='skill-button'
            onClick={() => console.log(`Activated skill: ${skill}`)}
        >
            {skill}
        </button>
    )
}
