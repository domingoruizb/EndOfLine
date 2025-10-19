package es.us.dp1.lx_xy_24_25.endofline.game;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Integer> {

    List<Game> findByHost(es.us.dp1.lx_xy_24_25.endofline.user.User host);

    List<Game> findByWinner(es.us.dp1.lx_xy_24_25.endofline.user.User winner);
}
