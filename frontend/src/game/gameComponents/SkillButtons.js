import SkillButton from './SkillButton'
import { SKILLS } from '../gameUtils/utils'
import '../../static/css/game/gameSkill.css'

export default function SkillButtons ({
    game
}) {
    return !game.spectating && (
        <div
            className='skills-container'
        >
            {
                SKILLS.map((skill, index) => {
                    return (
                        <SkillButton
                            key={index}
                            skill={skill}
                            game={game}
                        />
                    )
                })
            }
        </div>
    )
}
