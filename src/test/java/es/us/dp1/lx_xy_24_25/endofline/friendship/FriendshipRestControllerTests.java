package es.us.dp1.lx_xy_24_25.endofline.friendship;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.endofline.auth.AuthService;
import es.us.dp1.lx_xy_24_25.endofline.user.Authorities;
import es.us.dp1.lx_xy_24_25.endofline.user.AuthoritiesService;
import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;

@WebMvcTest(controllers = FriendshipRestController.class, excludeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))

class FriendshipRestControllerTests {
    
    private static final String BASE_URL = "/api/v1/friendships";
    private static final Integer TEST_FRIENDSHIP_ID = 1;
    private static final Integer TEST_USER_ID = 1;

    @SuppressWarnings("unused")
    @Autowired
    private FriendshipRestController friendshipController;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
	private ObjectMapper objectMapper;

    private User user;
    private User user2;
    private User user3;
    private User user4;
    private Authorities authority;
    private Friendship friendship;
    private Friendship friendship2;
    private Friendship friendship3;
    private Friendship friendship4;

    @BeforeEach
    void setup() {
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("ADMIN");

        LocalDate birthDate = LocalDate.of(1999, 01, 01);
		String avatar = "https://cdn-icons-png.flaticon.com/512/147/147144.png";

        authority = new Authorities();
        authority.setId(2);
        authority.setAuthority("PLAYER");

        user = new User();
        user.setId(1);
        user.setName("userName");
        user.setSurname("userSurname");
        user.setPassword("Play3r!");
        user.setEmail("user@gmail.com");
        user.setBirthdate(birthDate);
        user.setAuthority(authority);
        user.setAvatar(avatar);

        user2 = new User();
        user2.setId(2);
        user2.setName("user2Name");
        user2.setSurname("user2Surname");
        user2.setPassword("PLay3r!");
        user2.setEmail("user2@gmail.com");
        user2.setBirthdate(birthDate);
        user2.setAuthority(authority);
        user2.setAvatar(avatar);

        user3 = new User();
        user3.setId(3);
        user3.setName("user3Name");
        user3.setSurname("user3Surname");
        user3.setPassword("PLay3r!");
        user3.setEmail("user3@gmail.com");
        user3.setBirthdate(birthDate);
        user3.setAuthority(authority);
        user3.setAvatar(avatar);

        user4 = new User();
        user4.setId(4);
        user4.setName("user4Name");
        user4.setSurname("user4Surname");
        user4.setPassword("PLay3r!");
        user4.setEmail("user4@gmail.com");
        user4.setBirthdate(birthDate);
        user4.setAuthority(authority);
        user4.setAvatar(avatar);

        friendship = new Friendship();
        friendship.setId(1);
        friendship.setSender(user);
        friendship.setReceiver(user2);
        friendship.setFriendState(FriendStatus.ACCEPTED);

        friendship2 = new Friendship();
        friendship2.setId(2);
        friendship2.setSender(user3);
        friendship2.setReceiver(user);
        friendship2.setFriendState(FriendStatus.PENDING);

        friendship3 = new Friendship();
        friendship3.setId(3);
        friendship3.setSender(user2);
        friendship3.setReceiver(user3);
        friendship3.setFriendState(FriendStatus.PENDING);
    }

    @Test
    @WithMockUser(username = "admin1", password = "Adm1n!")
    void adminShouldFindAllFriendships() throws Exception {
        when(this.friendshipService.findAll()).thenReturn(List.of(friendship, friendship2, friendship3));

        mockMvc.perform(get(BASE_URL + "/all")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(3))
        .andExpect(jsonPath("$[?(@.id == 1)].sender.name").value("userName"))
        .andExpect(jsonPath("$[?(@.id == 2)].sender.name").value("user3Name"))
        .andExpect(jsonPath("$[?(@.id == 3)].sender.name").value("user2Name"));
    }

    @Test
    @WithMockUser(username = "userName", password = "Own3r!")
    void userShouldFindById() throws Exception {
        when(this.friendshipService.findById(1)).thenReturn(friendship);

        mockMvc.perform(get(BASE_URL + "/{id}", TEST_FRIENDSHIP_ID)).andExpect(status().isOk())
        .andExpect(jsonPath("$.sender.name").value("userName"))
        .andExpect(jsonPath("$.receiver.name").value("user2Name"));
    }

    @Test
    @WithMockUser(username = "userName", password = "Own3r!")
    void userShouldDeleteFriendship() throws Exception{
        when(this.friendshipService.findById(TEST_FRIENDSHIP_ID)).thenReturn(friendship);

        doNothing().when(this.friendshipService).delete(TEST_FRIENDSHIP_ID);
        mockMvc.perform(delete(BASE_URL + "/{id}", TEST_FRIENDSHIP_ID).with(csrf()))
        .andExpect(status().isOk());
    }

}
