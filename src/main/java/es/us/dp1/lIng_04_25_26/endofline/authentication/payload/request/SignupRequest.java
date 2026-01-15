package es.us.dp1.lIng_04_25_26.endofline.authentication.payload.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String name;

	@NotBlank
	private String surname;

	@NotBlank
	private String email;

	@NotBlank
	private String birthdate;

	@NotBlank
	private String authority;

	private String avatar;
}
