package es.us.dp1.lx_xy_24_25.endofline.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(es.us.dp1.lx_xy_24_25.endofline.exceptions.friendship.FriendshipForbiddenException.class)
    @ResponseBody
    public ResponseEntity<Object> handleFriendshipForbiddenException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(ex.getMessage()));
    }

    static class ErrorMessage {
        public String message;
        public ErrorMessage(String message) { this.message = message; }
    }
}
