package es.us.dp1.lIng_04_25_26.endofline.friendship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.enums.FriendStatus;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipBadRequestException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipForbiddenException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.friendship.FriendshipNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

@ExtendWith(MockitoExtension.class)
public class FriendshipServiceTests {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendshipService friendshipService;

    private User user1;
    private User user2;
    private User user3;
    private Friendship friendshipPending;
    private Friendship friendshipAccepted;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1);
        user1.setUsername("player1");

        user2 = new User();
        user2.setId(2);
        user2.setUsername("player2");

        user3 = new User();
        user3.setId(3);
        user3.setUsername("player3");

        friendshipPending = Friendship.build(user1, user2, FriendStatus.PENDING);
        friendshipPending.setId(10);

        friendshipAccepted = Friendship.build(user1, user2, FriendStatus.ACCEPTED);
        friendshipAccepted.setId(11);
    }


    @Test
    void testGetFriendshipFound() {
        when(friendshipRepository.findById(10)).thenReturn(Optional.of(friendshipPending));

        Friendship result = friendshipService.getFriendship(10);
        
        assertNotNull(result);
        assertEquals(10, result.getId());
    }

    @Test
    void testGetFriendshipNotFound() {
        when(friendshipRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(FriendshipNotFoundException.class, () -> {
            friendshipService.getFriendship(999);
        });
    }


    @Test
    void testGetFriendshipsByUser() {
        when(friendshipRepository.findFriendships(user1.getId())).thenReturn(List.of(friendshipAccepted));

        List<Friendship> result = friendshipService.getFriendships(user1);
        
        assertEquals(1, result.size());
        assertEquals(FriendStatus.ACCEPTED, result.get(0).getFriendState());
    }

    @Test
    void testGetPendingFriendships() {
        when(friendshipRepository.findPending(user1.getId())).thenReturn(List.of(friendshipPending));

        List<Friendship> result = friendshipService.getPendingFriendships(user1);

        assertEquals(1, result.size());
        assertEquals(FriendStatus.PENDING, result.get(0).getFriendState());
    }


    @Test
    void testCreateFriendshipSuccess() {
        when(userService.findCurrentUser()).thenReturn(user1);
        when(friendshipRepository.findFriendship(user1.getId(), user2.getId())).thenReturn(Optional.empty());
        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(i -> i.getArguments()[0]);

        Friendship created = friendshipService.create(user2);

        assertNotNull(created);
        assertEquals(user1, created.getSender());
        assertEquals(user2, created.getReceiver());
        assertEquals(FriendStatus.PENDING, created.getFriendState());
    }


    @Test
    void testCreateFriendshipWithSelf() {
        when(userService.findCurrentUser()).thenReturn(user1);

        assertThrows(FriendshipBadRequestException.class, () -> {
            friendshipService.create(user1);
        }, "You cannot be friends with yourself");
    }


    @Test
    void testCreateFriendshipAlreadyPending() {
        when(userService.findCurrentUser()).thenReturn(user1);
        
        Friendship existing = Friendship.build(user1, user2, FriendStatus.PENDING);
        when(friendshipRepository.findFriendship(user1.getId(), user2.getId())).thenReturn(Optional.of(existing));

        assertThrows(FriendshipBadRequestException.class, () -> {
            friendshipService.create(user2);
        }, "There is already a pending friendship request with this user");
    }

    @Test
    void testCreateFriendshipAlreadyAccepted() {
        when(userService.findCurrentUser()).thenReturn(user1);
        
        Friendship existing = Friendship.build(user1, user2, FriendStatus.ACCEPTED);
        when(friendshipRepository.findFriendship(user1.getId(), user2.getId())).thenReturn(Optional.of(existing));

        assertThrows(FriendshipBadRequestException.class, () -> {
            friendshipService.create(user2);
        }, "You are already friends with this user");
    }


    @Test
    void testAcceptFriendshipSuccess() {
        when(userService.findCurrentUser()).thenReturn(user2);
        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(i -> i.getArguments()[0]);

        Friendship result = friendshipService.accept(friendshipPending);

        assertEquals(FriendStatus.ACCEPTED, result.getFriendState());
        verify(friendshipRepository).save(friendshipPending);
    }

    @Test
    void testAcceptFriendshipNotPending() {
        when(userService.findCurrentUser()).thenReturn(user2);
        friendshipAccepted.setReceiver(user2);

        assertThrows(FriendshipBadRequestException.class, () -> {
            friendshipService.accept(friendshipAccepted);
        }, "The friendship has already been accepted");
    }

    @Test
    void testAcceptFriendshipForbiddenNotReceiver() {
        when(userService.findCurrentUser()).thenReturn(user1);

        assertThrows(FriendshipForbiddenException.class, () -> {
            friendshipService.accept(friendshipPending);
        }, "Only the receiver can accept the friendship");
    }


    @Test
    void testRejectFriendshipSuccess() {
        when(userService.findCurrentUser()).thenReturn(user2);
        doNothing().when(friendshipRepository).delete(friendshipPending);

        friendshipService.reject(friendshipPending);

        verify(friendshipRepository, times(1)).delete(friendshipPending);
    }

    @Test
    void testRejectFriendshipForbiddenNotReceiver() {
        when(userService.findCurrentUser()).thenReturn(user1);

        assertThrows(FriendshipForbiddenException.class, () -> {
            friendshipService.reject(friendshipPending);
        }, "Only the receiver can reject the friendship");
    }


    @Test
    void testDeleteFriendshipSuccessAsSender() {
        when(userService.findCurrentUser()).thenReturn(user1);
        doNothing().when(friendshipRepository).delete(friendshipAccepted);

        friendshipService.delete(friendshipAccepted);

        verify(friendshipRepository, times(1)).delete(friendshipAccepted);
    }

    @Test
    void testDeleteFriendshipSuccessAsReceiver() {
        friendshipAccepted.setReceiver(user2);
        when(userService.findCurrentUser()).thenReturn(user2);
        doNothing().when(friendshipRepository).delete(friendshipAccepted);

        friendshipService.delete(friendshipAccepted);

        verify(friendshipRepository, times(1)).delete(friendshipAccepted);
    }

    @Test
    void testDeleteFriendshipForbidden() {
        when(userService.findCurrentUser()).thenReturn(user3);

        assertThrows(FriendshipForbiddenException.class, () -> {
            friendshipService.delete(friendshipAccepted);
        }, "You are not authorized to delete this friendship.");
    }
}