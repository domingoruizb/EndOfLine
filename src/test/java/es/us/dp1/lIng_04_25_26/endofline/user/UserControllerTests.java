package es.us.dp1.lIng_04_25_26.endofline.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import es.us.dp1.lIng_04_25_26.endofline.authority.AuthorityService;
import es.us.dp1.lIng_04_25_26.endofline.configuration.SecurityConfiguration;
import es.us.dp1.lIng_04_25_26.endofline.game.GameService;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.user.UserNotFoundException;

@WebMvcTest(UserController.class)
class UserControllerTests {

    private static final int TEST_USER_ID = 1;
    private static final String BASE_URL = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    private Authority adminAuth;
    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setup() {
        adminAuth = new Authority();
        adminAuth.setId(1);
        adminAuth.setType("ADMIN");

        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setUsername("testuser");
        testUser.setName("Juan");
        testUser.setSurname("Alcalá");
        testUser.setEmail("test@example.com");
        testUser.setBirthdate(LocalDate.of(1990, 1, 1));
        testUser.setPassword("password123");
        testUser.setAuthority(adminAuth);

        testUserDTO = new UserDTO(testUser);
        when(userService.findCurrentUser()).thenReturn(testUser);
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFindAllPaginated() throws Exception {
        Page<User> page = new PageImpl<>(List.of(testUser));
        when(userService.findAllExceptMyself(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFindById() throws Exception {
        when(userService.findUser(TEST_USER_ID)).thenReturn(testUser);

        mockMvc.perform(get(BASE_URL + "/{id}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testReturn404WhenUserNotFound() throws Exception {
        when(userService.findUser(99)).thenThrow(new UserNotFoundException(99));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFindByUsername() throws Exception {
        when(userService.findUser("testuser")).thenReturn(testUser);

        mockMvc.perform(get(BASE_URL + "/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFindMySelf() throws Exception {
        when(userService.findCurrentUser()).thenReturn(testUser);

        mockMvc.perform(get(BASE_URL + "/myself"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testCreateUser() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post(BASE_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateUser() throws Exception {
        when(userService.findUser(TEST_USER_ID)).thenReturn(testUser);
        when(userService.updateUser(any(User.class), any(UserDTO.class))).thenReturn(testUser);

        mockMvc.perform(put(BASE_URL + "/{id}", TEST_USER_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testReturnNotFoundUpdateUser() throws Exception {
        when(userService.findUser(99)).thenThrow(new UserNotFoundException(99));

        mockMvc.perform(put(BASE_URL + "/99").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteUser() throws Exception {
        when(userService.findUser(TEST_USER_ID)).thenReturn(testUser);
        doNothing().when(gameService).deleteUser(any(User.class));

        mockMvc.perform(delete(BASE_URL + "/{id}", TEST_USER_ID).with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testReturnNotFoundDeleteUser() throws Exception {
        when(userService.findUser(99)).thenThrow(new UserNotFoundException(99));

        mockMvc.perform(delete(BASE_URL + "/99").with(csrf()))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFailCreateUserWhenDataIsInvalid() throws Exception {
        User invalidUser = new User();
        
        mockMvc.perform(post(BASE_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFailCreateUserWithDuplicatedEmail() throws Exception {
        when(userService.saveUser(any(User.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post(BASE_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testReturn404WhenUsernameNotFound() throws Exception {
        when(userService.findUser("nonexistent")).thenThrow(new UserNotFoundException("nonexistent"));

        mockMvc.perform(get(BASE_URL + "/username/nonexistent"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFailUpdateUserWhenUsernameAlreadyExists() throws Exception {
        when(userService.findUser(TEST_USER_ID)).thenReturn(testUser);
        when(userService.updateUser(any(User.class), any(UserDTO.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        mockMvc.perform(put(BASE_URL + "/{id}", TEST_USER_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testReturn401WhenAnonymousAccess() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFindAllAuthoritiesCorrectly() throws Exception {
        when(authorityService.findAll()).thenReturn(List.of(adminAuth));

        mockMvc.perform(get(BASE_URL + "/authorities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].type").value("ADMIN"));
    }

}