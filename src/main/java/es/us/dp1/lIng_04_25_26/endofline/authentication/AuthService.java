package es.us.dp1.lIng_04_25_26.endofline.authentication;

import es.us.dp1.lIng_04_25_26.endofline.authentication.payload.request.SignupRequest;
import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import es.us.dp1.lIng_04_25_26.endofline.authority.AuthorityService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final PasswordEncoder encoder;
	private final AuthorityService authorityService;
	private final UserService userService;

	@Autowired
	public AuthService(
        PasswordEncoder encoder,
        AuthorityService authorityService,
        UserService userService
    ) {
		this.encoder = encoder;
		this.authorityService = authorityService;
		this.userService = userService;
	}

	@Transactional
	public void createUser(@Valid SignupRequest request) {
        String password = encoder.encode(request.getPassword());
		String roles = request.getAuthority().toUpperCase();
		Authority authority = roles.equals("ADMIN")
                ? authorityService.findAuthorityByType("ADMIN")
                : authorityService.findAuthorityByType("PLAYER");

        User user = User.build(
            request,
            password,
            authority
        );

		userService.saveUser(user);
	}

}
