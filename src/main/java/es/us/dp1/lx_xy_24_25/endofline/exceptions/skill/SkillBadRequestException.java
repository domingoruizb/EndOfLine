package es.us.dp1.lx_xy_24_25.endofline.exceptions.skill;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SkillBadRequestException extends RuntimeException {
    public SkillBadRequestException() {
        super("Not possible to request a skill");
    }
}
