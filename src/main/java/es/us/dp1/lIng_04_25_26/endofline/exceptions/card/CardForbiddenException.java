package es.us.dp1.lIng_04_25_26.endofline.exceptions.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CardForbiddenException extends RuntimeException {
    public CardForbiddenException() {
        super("Not a valid card placement");
    }
}
