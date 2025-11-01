package es.us.dp1.lx_xy_24_25.endofline.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.endofline.auth.AuthService;
import es.us.dp1.lx_xy_24_25.endofline.auth.payload.request.SignupRequest;
import es.us.dp1.lx_xy_24_25.endofline.user.AuthoritiesService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Users & Admin Module")
@Feature("Authentication")
@Owner("DP1-tutors")
@SpringBootTest
public class AuthServiceTests {

	@Autowired
	protected AuthService authService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected AuthoritiesService authoritiesService;

	@Test
	@Transactional
	public void shouldCreateAdminUser() {
		SignupRequest request = createRequest("ADMIN", "admin2");
		int userFirstCount = ((Collection<User>) this.userService.findAll()).size();
		this.authService.createUser(request);
		int userLastCount = ((Collection<User>) this.userService.findAll()).size();
		assertEquals(userFirstCount + 1, userLastCount);
	}



	@Test
	@Transactional
	public void shouldCreatePlayerUser() {
		SignupRequest request = createRequest("PLAYER", "playertest");
		int userFirstCount = ((Collection<User>) this.userService.findAll()).size();
		//int playerFirstCount = ((Collection<Player>) this.playerService.findAll()).size();
		System.out.println("first number of users: " + userFirstCount);
		this.authService.createUser(request);
		int userLastCount = ((Collection<User>) this.userService.findAll()).size();
		//int playerLastCount = ((Collection<Player>) this.playerService.findAll()).size();
		System.out.println("last number of users: " + userLastCount);
		assertEquals(userFirstCount + 1, userLastCount);
		//assertEquals(playFirstCount + 1, playerLastCount);
	}

	private SignupRequest createRequest(String auth, String username) {
		SignupRequest request = new SignupRequest();
		request.setAuthority(auth);
		request.setName("prueba");
		request.setSurname("prueba");
		request.setPassword("prueba");
		request.setUsername(username);
		request.setEmail("prueba");
		request.setBirthdate("2000-01-01");
		request.setAvatar("avatar");

		if(auth == "PLAYER") {
			User playerUser = new User();
			playerUser.setUsername("clinicOwnerTest");
			playerUser.setPassword("clinicOwnerTest");
			playerUser.setName("clinicOwnerTest");
			playerUser.setSurname("clinicOwnerTest");
			playerUser.setEmail("clinicOwnerTest");
			playerUser.setBirthdate(LocalDate.parse("1990-01-01"));
			playerUser.setAvatar("avatar");
			playerUser.setAuthority(authoritiesService.findByAuthority("PLAYER"));
			userService.saveUser(playerUser);
		}

		return request;
	}

}
