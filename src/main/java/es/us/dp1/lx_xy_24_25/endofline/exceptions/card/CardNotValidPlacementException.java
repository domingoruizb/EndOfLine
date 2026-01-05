package es.us.dp1.lx_xy_24_25.endofline.exceptions.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CardNotValidPlacementException extends RuntimeException {
    public CardNotValidPlacementException() {
        super("Not a valid card placement");
    }
}
