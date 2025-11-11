package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface GamePlayerCardRepository extends CrudRepository<GamePlayerCard, Integer> {

    // Get all cards visible to a specific viewer (their own + on-board)
    @Query("SELECT gpc FROM GamePlayerCard gpc " +
           "JOIN FETCH gpc.card c " +
           "WHERE gpc.gamePlayer.game.id = :gameId " +
           "AND (gpc.onBoard = TRUE OR gpc.gamePlayer.id = :viewerId)")
    List<GamePlayerCard> findVisibleByGameIdForViewer(@Param("gameId") Integer gameId,
                                                      @Param("viewerId") Integer viewerId);

    // Get only on-board cards (publicly visible)
    @Query("SELECT gpc FROM GamePlayerCard gpc " +
           "JOIN FETCH gpc.card c " +
           "WHERE gpc.gamePlayer.game.id = :gameId AND gpc.onBoard = TRUE")
    List<GamePlayerCard> findOnBoardByGameId(@Param("gameId") Integer gameId);
}
