package es.us.dp1.lIng_04_25_26.endofline.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import es.us.dp1.lIng_04_25_26.endofline.authority.AuthorityService;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.user.AuthorityNotFoundException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Users & Admin Module")
@Feature("Authorization")
@Owner("DP1-tutors")
@SpringBootTest
@AutoConfigureTestDatabase
class AuthoritiesServiceTests {

	@Autowired
	private AuthorityService authorityService;


	@Test
	void testFindAllAuthorities() {
		List<Authority> auths = (List<Authority>) this.authorityService.findAll();
		assertEquals(2, auths.size());
	}


	@Test
    void testFindAuthorityByType() {
        Authority auth = this.authorityService.findAuthorityByType("ADMIN");
        assertNotNull(auth);
        assertEquals("ADMIN", auth.getType());
    }


	@Test
    void testFindAuthorityByTypePrefix() {
        Authority auth = this.authorityService.findAuthorityByType("ADM");
        assertNotNull(auth);
        assertEquals("ADMIN", auth.getType());
    }


	@Test
    void testNotFindAuthorityByIncorrectType() {
        assertThrows(AuthorityNotFoundException.class, () -> {
            this.authorityService.findAuthorityByType("NON_EXISTENT_AUTH");
        });
    }

}
