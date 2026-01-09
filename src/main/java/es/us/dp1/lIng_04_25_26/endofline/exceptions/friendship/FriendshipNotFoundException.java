package es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException(Integer friendshipId) {
        super("Friendship with ID " + friendshipId + " not found");
    }
}
