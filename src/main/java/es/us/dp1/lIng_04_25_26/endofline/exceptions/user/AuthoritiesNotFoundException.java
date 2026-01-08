package es.us.dp1.lIng_04_25_26.endofline.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AuthoritiesNotFoundException extends RuntimeException {
    public AuthoritiesNotFoundException(String name) {
        super("Authorities with name " + name + " not found");
    }
}
