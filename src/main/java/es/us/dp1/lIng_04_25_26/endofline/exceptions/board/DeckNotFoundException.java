package es.us.dp1.lIng_04_25_26.endofline.exceptions.board;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(Integer cardId) {
        super("Card with ID " + cardId + " not found in deck");
    }
}
