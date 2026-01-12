package es.us.dp1.lIng_04_25_26.endofline.authentication;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.authentication.payload.request.SignupRequest;
import es.us.dp1.lIng_04_25_26.endofline.authority.AuthorityService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;
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
	protected AuthorityService authorityService;

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
        // Arrange
        SignupRequest request = createRequest("PLAYER", "newPlayerUser");
        int userFirstCount = ((Collection<User>) this.userService.findAll()).size();
        
        // Act
        this.authService.createUser(request);

        // Assert
        int userLastCount = ((Collection<User>) this.userService.findAll()).size();
        assertEquals(userFirstCount + 1, userLastCount);

        // Verificación adicional
        User savedUser = userService.findUser("newPlayerUser");
        assertNotNull(savedUser);
        assertEquals("PLAYER", savedUser.getAuthority().getType());
    }

    // Método auxiliar corregido: Solo crea el objeto SignupRequest
    private SignupRequest createRequest(String auth, String username) {
        SignupRequest request = new SignupRequest();
        request.setAuthority(auth);
        request.setName("TestName");
        request.setSurname("TestSurname");
        request.setPassword("password123");
        request.setUsername(username);
        request.setEmail(username + "@test.com"); // Email único basado en el usuario para evitar colisiones
        request.setBirthdate("2000-01-01");
        request.setAvatar("https://example.com/avatar.png");
        return request;
    }

}
