package es.us.dp1.lx_xy_24_25.endofline.auth;

import java.time.LocalDate;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.endofline.auth.payload.request.SignupRequest;
import es.us.dp1.lx_xy_24_25.endofline.user.Authorities;
import es.us.dp1.lx_xy_24_25.endofline.user.AuthoritiesService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;

@Service
public class AuthService {

	private final PasswordEncoder encoder;
	private final AuthoritiesService authoritiesService;
	private final UserService userService;
	//private final PlayerService playerService;
	

	@Autowired
	public AuthService(PasswordEncoder encoder, AuthoritiesService authoritiesService, UserService userService
			// PlayerService playerService
			) {
		this.encoder = encoder;
		this.authoritiesService = authoritiesService;
		this.userService = userService;
		//this.playerService = ownerService;		
	}

	@Transactional
	public void createUser(@Valid SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		String strRoles = request.getAuthority();
		Authorities role;

		switch (strRoles.toLowerCase()) {
		case "admin":
			role = authoritiesService.findByAuthority("ADMIN");
			user.setAuthority(role);
			break;
		default:
			role = authoritiesService.findByAuthority("PLAYER");
			user.setAuthority(role);
		}
		user.setAvatar(request.getAvatar());
		user.setEmail(request.getEmail());
		LocalDate birthdate = LocalDate.parse(request.getBirthdate());
		user.setBirthdate(birthdate);
		user.setName(request.getName());
		user.setSurname(request.getSurname());
		userService.saveUser(user);
	}

}