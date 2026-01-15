package es.us.dp1.lIng_04_25_26.endofline.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import es.us.dp1.lIng_04_25_26.endofline.authority.AuthorityService;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.user.UserNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.user.UserUnauthorizedException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Users & Admin Module")
@Feature("Users Management")
@Owner("DP1-tutors")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authService;


    @Test
    @WithMockUser(username = "player1", password = "0wn3r")
    void testFindCurrentUser() {
        User user = this.userService.findCurrentUser();
        assertEquals("player1", user.getUsername());
    }


    @Test
    @WithMockUser(username = "nonexistent")
    void testThrowExceptionWhenCurrentUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> this.userService.findCurrentUser());
    }


    @Test
    void testThrowExceptionWhenNoUserAuthenticated() {
        assertThrows(UserUnauthorizedException.class, () -> this.userService.findCurrentUser());
    }


    @Test
    void testFindAllUsers() {
        List<User> users = (List<User>) this.userService.findAll();
        assertFalse(users.isEmpty());
    }


    @Test
    void testFindUserByUsername() {
        User user = this.userService.findUser("admin1");
        assertNotNull(user);
        assertEquals("admin1", user.getUsername());
    }


    @Test
    void testFindUserById() {
        User admin = this.userService.findUser("admin1");
        User user = this.userService.findUser(admin.getId());
        assertEquals("admin1", user.getUsername());
    }


    @Test
    void testExistUser() {
        assertTrue(this.userService.existsUser("admin1"));
        assertFalse(this.userService.existsUser("ghostUser"));
    }


    @Test
    void testThrowExceptionWhenUsernameNotFound() {
        assertThrows(UserNotFoundException.class, () -> this.userService.findUser("wrongUser"));
    }


    @Test
    void testFindSingleUser() {
        User player = this.userService.findUser("player1");
        User user = this.userService.findUser(player.getId());
        assertEquals("player1", user.getUsername());
    }


    @Test
    void testNotFindSingleUserWithBadID() {
        assertThrows(UserNotFoundException.class, () -> this.userService.findUser(999999));
    }


    @Test
    void testNotExistUser() {
        assertFalse(this.userService.existsUser("player10000"));
    }


    @Test
    void testSaveUser() {
        int initialCount = ((Collection<User>) this.userService.findAll()).size();

        User user = new User();
        user.setUsername("UniqueNewUser");
        user.setPassword("pass");
        user.setName("Name");
        user.setSurname("Surname");
        user.setEmail("unique_new@example.com");
        user.setBirthdate(LocalDate.of(2000, 1, 1));
        
        Authority auth = authService.findAll().get(0);
        user.setAuthority(auth);

        userService.saveUser(user);

        assertEquals(initialCount + 1, ((Collection<User>) this.userService.findAll()).size());
        assertNotNull(user.getId());
    }


    @Test
    void testThrowExceptionSavingDuplicatedUsername() {
        User user = new User();
        user.setUsername("admin1");
        user.setEmail("completely_new_email@example.com");
        
        assertThrows(RuntimeException.class, () -> userService.saveUser(user));
    }


    @Test
    void testUpdateUserSuccessfully() {
        User userToUpdate = userService.findUser("admin1");
        UserDTO dto = new UserDTO(userToUpdate); 
        
        dto.setName("UpdateTest");
        dto.setEmail("unique.email.update@gmail.com");

        User updated = userService.updateUser(userToUpdate, dto);

        assertEquals("UpdateTest", updated.getName());
        assertEquals("unique.email.update@gmail.com", updated.getEmail());
    }


    @Test
    void testThrowExceptionWhenUpdatingToExistingEmail() {
        User admin = userService.findUser("admin1");
        User player = userService.findUser("player1");

        UserDTO dto = new UserDTO(admin);
        dto.setEmail(player.getEmail());

        assertThrows(RuntimeException.class, () -> userService.updateUser(admin, dto));
    }


    @Test
    void testDeleteUser() {
        User user = userService.findUser("admin1");
        int initialSize = ((Collection<User>) userService.findAll()).size();
        
        userService.deleteUser(user);
        
        int finalSize = ((Collection<User>) userService.findAll()).size();
        assertEquals(initialSize - 1, finalSize);
        assertThrows(UserNotFoundException.class, () -> userService.findUser("admin1"));
    }


    @Test
    void testFindUserRegisteringWhenUserExists() {
        User user = userService.findUserRegistering("player1");
        assertNotNull(user);
        assertEquals("player1", user.getUsername());
    }


    @Test
    void testFindUserRegisteringWhenUserNotExists() {
        User user = userService.findUserRegistering("nonexistentUser");
        assertNull(user);
    }


    @Test
    @WithMockUser(username = "admin1", password = "0wn3r")
    void testFindAllExceptMyself() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> users = userService.findAllExceptMyself(pageable);
        
        assertNotNull(users);
        assertFalse(users.isEmpty());
        
        boolean containsCurrentUser = users.getContent().stream()
            .anyMatch(u -> u.getUsername().equals("admin1"));
        assertFalse(containsCurrentUser);
    }

}