package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
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
