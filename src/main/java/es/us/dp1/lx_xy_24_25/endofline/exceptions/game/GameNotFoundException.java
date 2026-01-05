package es.us.dp1.lx_xy_24_25.endofline.exceptions.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Integer gameId) {
        super("Game with ID " + gameId + " not found");
    }

    public GameNotFoundException(String code) {
        super("Game with code " + code + " not found");
    }
}
