package es.us.dp1.lx_xy_24_25.endofline.exceptions.friendship;

import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FriendshipNotValidException extends RuntimeException {
    public FriendshipNotValidException(Integer senderId, Integer receiverId) {
        super("Friendship between sender ID " + senderId + " and receiver ID " + receiverId + " is not valid");
    }

    public FriendshipNotValidException(User sender, User receiver) {
        super("Friendship between sender " + sender.getName() + " and receiver " + receiver.getName() + " is not valid");
    }

    public FriendshipNotValidException(String message) {
        super(message);
    }
}
