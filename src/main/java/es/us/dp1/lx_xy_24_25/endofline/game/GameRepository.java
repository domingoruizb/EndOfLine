package es.us.dp1.lx_xy_24_25.endofline.game;

import org.springframework.data.repository.CrudRepository;
import es.us.dp1.lx_xy_24_25.endofline.user.User;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Integer> {

    List<Game> findByHost(User host);

    List<Game> findByWinner(User winner);

    Optional<Game> getGameByCode(String code);
}
