package es.us.dp1.lx_xy_24_25.endofline.exceptions.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TurnForbiddenException extends RuntimeException {
    public TurnForbiddenException() {
        super("Not a valid turn");
    }
}
