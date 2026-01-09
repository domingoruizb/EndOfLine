package es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class FriendshipForbiddenException extends RuntimeException {
    public FriendshipForbiddenException(String message) {
        super(message);
    }
}
