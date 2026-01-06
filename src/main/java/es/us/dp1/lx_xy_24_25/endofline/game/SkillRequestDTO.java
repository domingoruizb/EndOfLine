package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;

// TODO: Could use @Getter and @Setter from Lombok to reduce boilerplate code
public class SkillRequestDTO {
    private String skill;

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public Skill toSkillEnum() {
        return Skill.valueOf(this.skill);
    }
}
