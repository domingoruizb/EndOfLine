package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Integer> {

    List<Game> findByHost(User host);

    List<Game> findByWinner(User winner);

    Optional<Game> getGameByCode(String code);

    @Query("SELECT COUNT(g) FROM Game g JOIN g.gamePlayers gp WHERE gp.user.id = ?1 AND g.endedAt IS NOT NULL")
    long countGamesPlayedByUser(Integer userId);

    @Query("SELECT COUNT(g) FROM Game g WHERE g.winner.id = ?1")
    long countGameWinsByUser(Integer userId);

    @Query("SELECT g FROM Game g JOIN g.gamePlayers gp WHERE gp.user.id = ?1 AND g.endedAt IS NOT NULL AND g.startedAt IS NOT NULL")
    List<Game> findFinishedGamesByUser(Integer userId);

}
