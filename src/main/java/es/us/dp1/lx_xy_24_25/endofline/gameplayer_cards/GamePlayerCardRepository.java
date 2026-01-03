package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamePlayerCardRepository extends CrudRepository<GamePlayerCard, Integer> {

    @Query("SELECT gpc FROM GamePlayerCard gpc WHERE gpc.gamePlayer.game.id = :gameId ORDER BY gpc.placedAt")
    List<GamePlayerCard> findByGameId (Integer gameId);

    @Query("SELECT gpc FROM GamePlayerCard gpc WHERE gpc.gamePlayer.id = :gamePlayerId ORDER BY gpc.placedAt DESC")
    List<GamePlayerCard> findPlacedCards (Integer gamePlayerId);

    @Query("SELECT gpc FROM GamePlayerCard gpc WHERE gpc.gamePlayer = :gamePlayer ORDER BY gpc.placedAt DESC")
    List<GamePlayerCard> findLastPlacedCards(GamePlayer gamePlayer);
}
