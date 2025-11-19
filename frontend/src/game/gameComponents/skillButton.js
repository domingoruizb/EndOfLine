import React from 'react';
import { setUpSkill } from '../gameUtils/apiUtils.js';

export default function SkillButton ({
    skill,
    gameId,
    userId,
    isDisabled,
    activeSkill
}) {
    const handleClick = () => {
        if (!isDisabled) {
            const formattedSkill = skill.toUpperCase().replace(' ', '_');
            setUpSkill(formattedSkill, gameId, userId);
        }
    };

    const buttonSkillName = skill.toUpperCase().replace(' ', '_');
    const isActive = activeSkill === buttonSkillName;

    const buttonClasses = `skill-button 
        ${isDisabled ? 'skill-button-disabled' : ''} 
        ${isActive ? 'skill-button-active' : ''}
    `;
    return (
        <button
            className={buttonClasses}
            onClick={handleClick}
            disabled={isDisabled}
        >
            {skill}
        </button>
    )
}
