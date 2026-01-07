package es.us.dp1.lx_xy_24_25.endofline.exceptions.friendship;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FriendshipBadRequestException extends RuntimeException {
    public FriendshipBadRequestException(String message) {
        super(message);
    }
}
