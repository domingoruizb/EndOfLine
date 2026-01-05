package es.us.dp1.lx_xy_24_25.endofline.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super("User with ID " + userId + " not found");
    }

    public UserNotFoundException(String username) {
        super("User with username " + username + " not found");
    }
}
