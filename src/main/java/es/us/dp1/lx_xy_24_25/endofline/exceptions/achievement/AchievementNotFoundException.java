package es.us.dp1.lx_xy_24_25.endofline.exceptions.achievement;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AchievementNotFoundException extends RuntimeException {
    public AchievementNotFoundException(Integer achievementId) {
        super("Achievement with id " + achievementId + " not found");
    }
}
