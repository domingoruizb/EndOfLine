package es.us.dp1.lx_xy_24_25.endofline.exceptions.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TurnNotValidException extends RuntimeException {
    public TurnNotValidException() {
        super("Not a valid turn");
    }
}
