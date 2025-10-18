package es.us.dp1.lx_xy_24_25.endofline.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	
	// User
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
