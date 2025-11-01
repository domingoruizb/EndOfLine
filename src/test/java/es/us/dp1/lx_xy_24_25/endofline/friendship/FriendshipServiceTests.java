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
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;

@SpringBootTest
@AutoConfigureTestDatabase
class FriendshipServiceTests {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    private Friendship createFriendship(){
        Integer sender_id = 6;
        Integer receiver_id = 11;
        return this.friendshipService.create(sender_id, receiver_id);
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
        Friendship friendship1 = new Friendship();
        friendship1.setSender(userService.findUser(1));
        friendship1.setReceiver(userService.findUser(6));
        Integer sender_id = 1;
        Integer receiver_id = 6;
        Friendship friendship2 = this.friendshipService.create(sender_id, receiver_id);
        assertEquals(friendship2.getFriendState(), FriendStatus.PENDING);
        friendship1.setFriendState(FriendStatus.ACCEPTED);
        Friendship updatedFriendship = this.friendshipService.update(friendship2.getId(), friendship1);
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
