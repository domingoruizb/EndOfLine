package es.us.dp1.lx_xy_24_25.endofline.friendship;

import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer>{

    @Query("SELECT f FROM Friendship f WHERE f.sender.id = ?1 OR f.receiver.id = ?1")
    Iterable<Friendship> findFriendshipsByUserId(Integer id);

    @Query("SELECT f FROM Friendship f WHERE (f.sender.id = ?1 AND f.receiver.id = ?2) OR (f.sender.id = ?2 AND f.receiver.id = ?1)")
    Optional<Friendship> findFriendshipBySenderAndReceiver(Integer sender_id, Integer receiver_id);

    @Query("SELECT f FROM Friendship f WHERE f.receiver.id = :userId AND f.friendState = 'PENDING'")
    Iterable<Friendship> findPendingReceivedFriendships(@Param("userId") Integer userId);

    @Query("SELECT f FROM Friendship f WHERE (f.sender.id = :userId OR f.receiver.id = :userId) AND f.friendState = :status")
    List<Friendship> findFriendshipsByUser(Integer userId, FriendStatus status);

}
