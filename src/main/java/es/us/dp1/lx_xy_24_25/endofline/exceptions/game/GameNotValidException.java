package es.us.dp1.lx_xy_24_25.endofline.exceptions.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GameNotValidException extends RuntimeException {
    public GameNotValidException(String message) {
        super(message);
    }
}
