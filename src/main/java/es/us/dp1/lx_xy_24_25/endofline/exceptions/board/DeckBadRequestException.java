package es.us.dp1.lx_xy_24_25.endofline.exceptions.board;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeckBadRequestException extends RuntimeException {
    public DeckBadRequestException(GamePlayer gamePlayer) {
        super("Deck request for player " + gamePlayer.getId() + " is not valid");
    }
}
