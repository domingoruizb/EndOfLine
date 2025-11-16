package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamePlayerCardRepository extends CrudRepository<GamePlayerCard, Integer> {

    @Query("SELECT gpc FROM GamePlayerCard gpc WHERE gpc.gamePlayer.game.id = :gameId")
    List<GamePlayerCard> findByGameId(Integer gameId);

    @Query("SELECT gpc FROM GamePlayerCard gpc WHERE gpc.gamePlayer.id = :gamePlayerId ORDER BY gpc.placedAt DESC")
    List<GamePlayerCard> findByGamePlayerIdOrderByPlacedAtDesc(Integer gamePlayerId);

}
