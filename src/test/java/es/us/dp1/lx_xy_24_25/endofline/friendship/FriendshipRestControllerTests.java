package es.us.dp1.lx_xy_24_25.endofline.friendship;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.List;

import es.us.dp1.lx_xy_24_25.endofline.exceptions.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
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

//    @Test
//    @WithMockUser(username = "userName", password = "Own3r!")
//    void playerShouldFindHisAcceptedFriendships() throws Exception {
//        when(this.userService.findCurrentUser()).thenReturn(user);
//        when(this.friendshipService.findAcceptedFriendshipsOf(1)).thenReturn(List.of(friendship));
//
//        mockMvc.perform(get(BASE_URL + "/myAcceptedFriendships")).andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()").value(1))
//            .andExpect(jsonPath("$[0].sender.name").value("userName"))
//            .andExpect(jsonPath("$[0].receiver.name").value("user2Name"));
//    }

//    @Test
//    @WithMockUser(username = "user3Name", password = "Own3r!")
//    void playerShouldFindHisPendingFriendships() throws Exception {
//        when(this.userService.findCurrentUser()).thenReturn(user3);
//        when(this.friendshipService.findPendingFriendshipsOf(3)).thenReturn(List.of(friendship2, friendship3));
//
//        mockMvc.perform(get(BASE_URL + "/myPendingFriendships")).andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()").value(2));
//    }

    @Test
    @WithMockUser(username = "userName", password = "Own3r!")
    void playerShouldAcceptFriendship() throws Exception {
        final Integer PENDING_FRIENDSHIP_ID = friendship2.getId();

        when(this.userService.findCurrentUser()).thenReturn(user);

        Friendship acceptedFriendship = friendship2;
        acceptedFriendship.setFriendState(FriendStatus.ACCEPTED);

        when(this.friendshipService.acceptFriendShip(PENDING_FRIENDSHIP_ID)).thenReturn(acceptedFriendship);

        mockMvc.perform(put(BASE_URL + "/{id}/acceptFriendship", PENDING_FRIENDSHIP_ID)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(PENDING_FRIENDSHIP_ID))
            .andExpect(jsonPath("$.sender.name").value("user3Name"))
            .andExpect(jsonPath("$.receiver.name").value("userName"))
            .andExpect(jsonPath("$.friendState").value("ACCEPTED"));
    }

    @Test
    @WithMockUser(username = "user3Name", password = "Own3r!")
    void playerShouldBeForbiddenToAcceptFriendship() throws Exception { //because he is the sender, not the receiver
        final Integer PENDING_FRIENDSHIP_ID = friendship2.getId();

        when(this.userService.findCurrentUser()).thenReturn(user3);

        when(this.friendshipService.acceptFriendShip(PENDING_FRIENDSHIP_ID))
            .thenThrow(new AccessDeniedException("Only the receiver can accept the friendship."));

        mockMvc.perform(put(BASE_URL + "/{id}/acceptFriendship", PENDING_FRIENDSHIP_ID)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "userName", password = "Own3r!")
    void playerShouldCreateFriendship() throws Exception {

        final Integer SENDER_ID = user.getId();
        final Integer RECEIVER_ID = user4.getId();

        FriendshipDTO requestDto = new FriendshipDTO(SENDER_ID, RECEIVER_ID);

        Friendship createdFriendship = new Friendship();
        createdFriendship.setId(56);
        createdFriendship.setSender(user);
        createdFriendship.setReceiver(user4);
        createdFriendship.setFriendState(FriendStatus.PENDING);

        when(this.friendshipService.create(any(FriendshipDTO.class))).thenReturn(createdFriendship);

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(56))
            .andExpect(jsonPath("$.sender.name").value(user.getName()))
            .andExpect(jsonPath("$.receiver.name").value(user4.getName()))
            .andExpect(jsonPath("$.friendState").value("PENDING"));
    }



}
