package es.us.dp1.lIng_04_25_26.endofline.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer>{


	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

	Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

	@Query("""
        SELECT u FROM User u WHERE u.
            id <> :userId
        ORDER BY
            CASE WHEN u.authority.type = 'ADMIN' THEN 0 ELSE 1 END,
            u.username ASC
    """)
    Page<User> findAllExceptMySelf(Integer userId, Pageable pageable);

    // TODO: Implement if necessary
//    @Query("""
//        SELECT u FROM User u
//        WHERE u.id IN (
//            SELECT DISTINCT
//                CASE
//                    WHEN f.sender.id = :userId THEN f.receiver.id
//                    ELSE f.sender.id
//                END
//            FROM Friendship f
//            WHERE f.friendState = 'ACCEPTED'
//            AND (f.sender.id = :userId OR f.receiver.id = :userId)
//            AND EXISTS (
//                SELECT 1
//                FROM GamePlayer gp
//                JOIN gp.game g
//                WHERE gp.user.id = CASE
//                    WHEN f.sender.id = :userId THEN f.receiver.id
//                    ELSE f.sender.id
//                END
//                AND g.round > 0
//                AND g.endedAt IS NULL
//            )
//        )
//    """)
//    List<User> findFriendsInGame(Integer userId);

}
