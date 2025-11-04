package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GamePlayerRepository extends CrudRepository<GamePlayer, Integer> {

    Optional<GamePlayer> findByGameIdAndUserId(Integer gameId, Integer userId);
}
