package es.us.dp1.lIng_04_25_26.endofline.friendship;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {

    @Query("SELECT f FROM Friendship f WHERE (f.sender.id = :senderId AND f.receiver.id = :receiverId) OR (f.sender.id = :receiverId AND f.receiver.id = :senderId)")
    Optional<Friendship> findFriendship(Integer senderId, Integer receiverId);

    @Query("SELECT f FROM Friendship f WHERE f.sender.id = :userId OR f.receiver.id = :userId")
    List<Friendship> findFriendships(Integer userId);

    @Query("SELECT f FROM Friendship f WHERE f.receiver.id = :userId AND f.friendState = 'PENDING'")
    List<Friendship> findPending(Integer userId);

}
