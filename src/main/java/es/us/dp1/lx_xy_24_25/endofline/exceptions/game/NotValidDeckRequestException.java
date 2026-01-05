package es.us.dp1.lx_xy_24_25.endofline.exceptions.game;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotValidDeckRequestException extends RuntimeException {
    public NotValidDeckRequestException(GamePlayer gamePlayer) {
        super("Deck request " + gamePlayer.getId() + " is not valid.");
    }
}
