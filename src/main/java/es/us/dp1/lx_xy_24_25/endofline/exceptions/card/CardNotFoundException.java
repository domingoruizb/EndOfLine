package es.us.dp1.lx_xy_24_25.endofline.exceptions.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Integer cardId) {
        super("Card with id " + cardId + " not found");
    }
}
