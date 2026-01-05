package es.us.dp1.lx_xy_24_25.endofline.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException() {
        super("User not authenticated");
    }
}
