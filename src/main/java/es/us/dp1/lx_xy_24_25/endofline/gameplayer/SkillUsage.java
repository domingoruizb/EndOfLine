package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
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

}
