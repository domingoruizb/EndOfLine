package es.us.dp1.lIng_04_25_26.endofline.gameplayer;

import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SkillUsage {

    @Enumerated(EnumType.STRING)
    private Skill skill;

    private Integer round;

    public static SkillUsage build(
        Skill skill,
        Integer round
    ) {
        SkillUsage skillUsage = new SkillUsage();
        skillUsage.setSkill(skill);
        skillUsage.setRound(round);
        return skillUsage;
    }

}
