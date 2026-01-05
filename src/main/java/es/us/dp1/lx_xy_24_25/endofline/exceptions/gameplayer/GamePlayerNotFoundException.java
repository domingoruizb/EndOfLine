package es.us.dp1.lx_xy_24_25.endofline.exceptions.gameplayer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GamePlayerNotFoundException extends RuntimeException {
    public GamePlayerNotFoundException(Integer userId, Integer gameId) {
        super("Player with id " + userId + " in game " + gameId + " not found");
    }

    public GamePlayerNotFoundException() {
        super("Player not found");
    }
}
