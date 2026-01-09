package es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FriendshipBadRequestException extends RuntimeException {
    public FriendshipBadRequestException(String message) {
        super(message);
    }
}
