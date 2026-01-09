package es.us.dp1.lIng_04_25_26.endofline.exceptions.board;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeckBadRequestException extends RuntimeException {
    public DeckBadRequestException(GamePlayer gamePlayer) {
        super("Deck request for player " + gamePlayer.getId() + " is not valid");
    }
}
