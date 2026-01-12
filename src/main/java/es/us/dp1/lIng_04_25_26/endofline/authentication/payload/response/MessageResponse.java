package es.us.dp1.lIng_04_25_26.endofline.authentication.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

	private String message;

	public MessageResponse(String message) {
	    this.message = message;
	}

}
