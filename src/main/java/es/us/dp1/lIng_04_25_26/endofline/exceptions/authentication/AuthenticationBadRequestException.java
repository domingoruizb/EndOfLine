package es.us.dp1.lIng_04_25_26.endofline.exceptions.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuthenticationBadRequestException extends RuntimeException {
    public AuthenticationBadRequestException() {
        super("There was a problem retrieving authentication data, please log in again");
    }
}
