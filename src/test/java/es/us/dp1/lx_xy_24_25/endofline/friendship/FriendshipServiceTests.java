package es.us.dp1.lx_xy_24_25.endofline.friendship;

import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.BadRequestException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class FriendshipServiceTests {

    @Autowired
    private FriendshipService friendshipService;

    @Test
    void testFindById() {
        Friendship friendship = friendshipService.findById(1);
        assertNotNull(friendship);
        assertEquals(1, friendship.getId());
        assertEquals(FriendStatus.ACCEPTED, friendship.getFriendState());
    }

    @Test
    void testFindByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                friendshipService.findById(9999)
        );
    }

    @Test
    void testCreateFriendship() {
        FriendshipDTO dto = new FriendshipDTO(13, 14);
        Friendship created = friendshipService.create(dto);
        assertNotNull(created);
        assertEquals(FriendStatus.PENDING, created.getFriendState());
        assertEquals(13, created.getSender().getId());
        assertEquals(14, created.getReceiver().getId());
    }

    @Test
    void testCreateFriendshipWithSelf() {
        FriendshipDTO dto = new FriendshipDTO(4, 4);
        assertThrows(BadRequestException.class, () ->
                friendshipService.create(dto)
        );
    }

    @Test
    void testCreateFriendshipAlreadyPending() {
        FriendshipDTO dto = new FriendshipDTO(4, 5);
        assertThrows(BadRequestException.class, () ->
                friendshipService.create(dto)
        );
    }

    @Test
    void testCreateFriendshipAlreadyAccepted() {
        FriendshipDTO dto = new FriendshipDTO(1, 4);
        assertThrows(BadRequestException.class, () ->
                friendshipService.create(dto)
        );
    }

    @Test
    void testCreateFriendshipUserNotExist() {
        FriendshipDTO dto = new FriendshipDTO(4, 9999);
        assertThrows(BadRequestException.class, () ->
                friendshipService.create(dto)
        );
    }

    @Test
    @WithMockUser(username = "player2", authorities = {"PLAYER"})
    void testAcceptFriendship() {
        Friendship accepted = friendshipService.acceptFriendShip(2);
        assertEquals(FriendStatus.ACCEPTED, accepted.getFriendState());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testAcceptFriendshipNotReceiver() {
        assertThrows(AccessDeniedException.class, () ->
                friendshipService.acceptFriendShip(2)
        );
    }

    @Test
    @WithMockUser(username = "player6", authorities = {"PLAYER"})
    void testRejectFriendship() {
        assertDoesNotThrow(() -> friendshipService.rejectFriendShip(9));
        assertThrows(ResourceNotFoundException.class, () -> friendshipService.findById(9));
    }

    @Test
    @WithMockUser(username = "player4", authorities = {"PLAYER"})
    void testRejectFriendshipNotReceiver() {
        assertThrows(AccessDeniedException.class, () ->
                friendshipService.rejectFriendShip(9)
        );
    }

    @Test
    void testDeleteFriendship() {
        FriendshipDTO dto = new FriendshipDTO(7, 10);
        Friendship f = friendshipService.create(dto);
        assertDoesNotThrow(() -> friendshipService.delete(f.getId()));
        assertThrows(ResourceNotFoundException.class, () ->
                friendshipService.findById(f.getId())
        );
    }
}
