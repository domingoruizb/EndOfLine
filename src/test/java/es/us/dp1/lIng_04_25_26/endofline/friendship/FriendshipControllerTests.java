package es.us.dp1.lIng_04_25_26.endofline.friendship;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lIng_04_25_26.endofline.enums.FriendStatus;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipBadRequestException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipForbiddenException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.friendship.FriendshipDTO;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
public class FriendshipControllerTests {

    private static final String BASE_URL = "/api/v1/friendships";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private UserService userService;

    private User currentUser;
    private User otherUser;
    private Friendship friendshipPending;
    private Friendship friendshipAccepted;
    private FriendshipDTO friendshipDTO;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("player1");
        currentUser.setEmail("player1@test.com");

        otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("player2");
        otherUser.setEmail("player2@test.com");

        friendshipPending = Friendship.build(otherUser, currentUser, FriendStatus.PENDING);
        friendshipPending.setId(10);

        friendshipAccepted = Friendship.build(currentUser, otherUser, FriendStatus.ACCEPTED);
        friendshipAccepted.setId(11);

        friendshipDTO = new FriendshipDTO("player2");
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testFindMyFriendships() throws Exception {
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(friendshipService.getFriendships(currentUser)).thenReturn(List.of(friendshipAccepted));

        mockMvc.perform(get(BASE_URL)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(11))
                .andExpect(jsonPath("$[0].friendState").value("ACCEPTED"));
    }


    @Test
    @WithMockUser(username = "player2", authorities = {"PLAYER"})
    void testFindPendingReceived() throws Exception {
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(friendshipService.getPendingFriendships(currentUser)).thenReturn(List.of(friendshipPending));

        mockMvc.perform(get(BASE_URL + "/pending")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].friendState").value("PENDING"));
    }


    @Test
    @WithMockUser(username = "player10", authorities = {"PLAYER"})
    void testCreateFriendship() throws Exception {
        when(userService.findUser("player2")).thenReturn(otherUser);
        
        Friendship newFriendship = Friendship.build(currentUser, otherUser, FriendStatus.PENDING);
        when(friendshipService.create(any(User.class))).thenReturn(newFriendship);

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friendshipDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sender.username").value("player1"))
                .andExpect(jsonPath("$.friendState").value("PENDING"));
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreateFriendshipValidationError() throws Exception {
        FriendshipDTO invalidDto = new FriendshipDTO("");

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest()); 
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreateFriendshipBusinessException() throws Exception {
        when(userService.findUser("player2")).thenReturn(otherUser);
        when(friendshipService.create(any(User.class)))
            .thenThrow(new FriendshipBadRequestException("You are already friends with this user"));

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friendshipDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "player2", authorities = {"PLAYER"})
    void testAcceptFriendship() throws Exception {
        when(friendshipService.getFriendship(10)).thenReturn(friendshipPending);
        
        Friendship acceptedResult = Friendship.build(otherUser, currentUser, FriendStatus.ACCEPTED);
        acceptedResult.setId(10);
        
        when(friendshipService.accept(friendshipPending)).thenReturn(acceptedResult);

        mockMvc.perform(put(BASE_URL + "/10/acceptFriendship")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendState").value("ACCEPTED"));
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreateFriendshipWithSelf() throws Exception {
        FriendshipDTO selfDto = new FriendshipDTO("player1");
        
        when(userService.findUser("player1")).thenReturn(currentUser);
        
        when(friendshipService.create(currentUser))
            .thenThrow(new FriendshipBadRequestException("You cannot be friends with yourself"));

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(selfDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreateDuplicateFriendship() throws Exception {
        when(userService.findUser("player2")).thenReturn(otherUser);
        when(friendshipService.create(any(User.class)))
            .thenThrow(new FriendshipBadRequestException("You are already friends with this user"));

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friendshipDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreateFriendshipAlreadyPending() throws Exception {
        when(userService.findUser("player2")).thenReturn(otherUser);
        when(friendshipService.create(any(User.class)))
            .thenThrow(new FriendshipBadRequestException("There is already a pending friendship request with this user"));

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friendshipDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testAcceptFriendshipNotFound() throws Exception {
        when(friendshipService.getFriendship(999)).thenThrow(new FriendshipNotFoundException(999));

        mockMvc.perform(put(BASE_URL + "/999/acceptFriendship")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "player6", authorities = {"PLAYER"})
    void testRejectFriendship() throws Exception {
        when(friendshipService.getFriendship(10)).thenReturn(friendshipPending);
        doNothing().when(friendshipService).reject(friendshipPending);

        mockMvc.perform(delete(BASE_URL + "/10/rejectFriendship")
                .with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "player4", authorities = {"PLAYER"})
    void testRejectFriendshipForbidden() throws Exception {
        when(friendshipService.getFriendship(10)).thenReturn(friendshipPending);
        doThrow(new FriendshipForbiddenException("Only the receiver can reject the friendship"))
            .when(friendshipService).reject(friendshipPending);

        mockMvc.perform(delete(BASE_URL + "/10/rejectFriendship")
                .with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testDeleteFriendship() throws Exception {
        when(friendshipService.getFriendship(11)).thenReturn(friendshipAccepted);
        doNothing().when(friendshipService).delete(friendshipAccepted);

        mockMvc.perform(delete(BASE_URL + "/11")
                .with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "player3", authorities = {"PLAYER"})
    void testDeleteFriendshipForbidden() throws Exception {
        when(friendshipService.getFriendship(999)).thenThrow(new FriendshipNotFoundException(999));

        mockMvc.perform(delete(BASE_URL + "/999")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

}
