package es.us.dp1.lIng_04_25_26.endofline.exceptions.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GameBadRequestException extends RuntimeException {
    public GameBadRequestException(String message) {
        super(message);
    }
}
