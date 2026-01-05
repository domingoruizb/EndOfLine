package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GamePlayerRepository extends CrudRepository<GamePlayer, Integer> {

    Optional<GamePlayer> findByGameIdAndUserId(Integer gameId, Integer userId);

    @Query("SELECT gp FROM GamePlayer gp WHERE gp.game.id = :gameId AND gp.id <> :gamePlayerId")
    Optional<GamePlayer> findOpponent(Integer gameId, Integer gamePlayerId);

    @Query("SELECT gp FROM GamePlayer gp WHERE gp.game.id = :gameId AND gp.user.id <> :userId")
    Optional<GamePlayer> findNextPlayer(Integer gameId, Integer userId);

    @Query("""
        SELECT gp FROM GamePlayer gp, Friendship f
        WHERE gp.game.id = :gameId
        AND f.friendState = 'ACCEPTED'
        AND (
            (f.sender.id = :userId AND f.receiver.id = gp.user.id) OR
            (f.receiver.id = :userId AND f.sender.id = gp.user.id)
        )
    """)
    List<GamePlayer> findFriendsInGame(Integer userId, Integer gameId);
}
