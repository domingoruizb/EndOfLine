package es.us.dp1.lIng_04_25_26.endofline.game;

import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillRequestDTO {
    private String skill;

    public Skill toSkillEnum() {
        return Skill.valueOf(this.skill);
    }
}
