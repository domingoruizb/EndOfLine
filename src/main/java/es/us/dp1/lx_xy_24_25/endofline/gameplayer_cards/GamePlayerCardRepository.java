package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerCardRepository extends CrudRepository<GamePlayerCard, Integer> {
}
