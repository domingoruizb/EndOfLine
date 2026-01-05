package es.us.dp1.lx_xy_24_25.endofline.exceptions.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotValidCardPlacementException extends RuntimeException {
    public NotValidCardPlacementException() {
        super("Not valid card placement!");
    }
}
