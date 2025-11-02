package es.us.dp1.lx_xy_24_25.endofline.friendship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase
class FriendshipServiceTests {

    @Autowired
    private FriendshipService friendshipService;

    private Friendship createFriendship(){
        FriendshipDTO friendShipDTO = new FriendshipDTO(6, 11);
        return this.friendshipService.create(friendShipDTO);
    }

    @Test
    void shouldFindFriendshipById() {
        Friendship testFriendship = friendshipService.findById(1);
        assert(testFriendship.getSender().getId() == 1);
        assert(testFriendship.getReceiver().getId() == 4);
        assert(testFriendship.getFriendState().equals(FriendStatus.ACCEPTED));
    }

    @Test
    void shouldNotFindFriendshipByBadId(){
        assertThrows(ResourceNotFoundException.class, () -> friendshipService.findById(1000));
    }

    @Test
    void shouldFindAllFriendships(){
        Iterable<Friendship> friendships = friendshipService.findAll();
        long count = StreamSupport.stream(friendships.spliterator(), false).count();
        assertEquals(12, count);
    }

    @Test
    void shouldFindAllFriendshipsByPlayerId(){
        Iterable<Friendship> friendships = friendshipService.findFriendshipsByUserId(1, FriendStatus.ACCEPTED);
        long count = StreamSupport.stream(friendships.spliterator(), false).count();
        assertEquals(3, count);
    }

    @Test
    @Transactional
    void shouldInsertFriendship(){
        Iterable<Friendship> friendships = friendshipService.findAll();
        long initialCount = StreamSupport.stream(friendships.spliterator(), false).count();
        createFriendship();
        Iterable<Friendship> friendships2 = friendshipService.findAll();
        long finalCount = StreamSupport.stream(friendships2.spliterator(), false).count();
        assertEquals(initialCount + 1, finalCount);
    }

    @Test
    @Transactional
    void shouldUpdateFriendship(){
        FriendshipDTO friendshipDTO = new FriendshipDTO(1, 6);
        Friendship friendship = this.friendshipService.create(friendshipDTO);
        assertEquals(friendship.getFriendState(), FriendStatus.PENDING);
        friendshipDTO.setFriendship_state(FriendStatus.ACCEPTED);
        Friendship updatedFriendship = this.friendshipService.update(friendship.getId(), friendshipDTO);
        assertEquals(updatedFriendship.getFriendState(), FriendStatus.ACCEPTED);
    }

    @Test
    @Transactional
    void shouldDeleteFriendship(){
        Friendship friendship = createFriendship();
        Iterable<Friendship> friendships = friendshipService.findAll();
        long initialCount = StreamSupport.stream(friendships.spliterator(), false).count();
        friendshipService.delete(friendship.getId());
        Iterable<Friendship> friendships2 = friendshipService.findAll();
        long finalCount = StreamSupport.stream(friendships2.spliterator(), false).count();
        assertEquals(initialCount - 1, finalCount);
    }
}
