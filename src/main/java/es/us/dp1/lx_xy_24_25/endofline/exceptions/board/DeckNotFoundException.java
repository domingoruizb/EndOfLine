package es.us.dp1.lx_xy_24_25.endofline.exceptions.board;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(Integer cardId) {
        super("Card with ID " + cardId + " not found in deck");
    }
}
