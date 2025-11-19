import React from 'react';
import { setUpSkill } from '../gameUtils/apiUtils.js';

export default function SkillButton ({
    skill,
    gameId,
    userId
}) {
    return (
        <button
            className='skill-button'
            onClick={() => {
                setUpSkill(skill.toUpperCase().replace(' ', '_'), gameId, userId);
            }}
        >
            {skill}
        </button>
    )
}
