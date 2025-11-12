package es.us.dp1.lx_xy_24_25.endofline.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    @Query("SELECT c FROM Card c WHERE c.color = :color AND c.initiative IS NOT NULL")
    List<Card> findByColorLine(Color color);

     @Query("SELECT c FROM Card c WHERE c.color = :color AND c.initiative IS NOT NULL")
    List<Card> findByColorStartCard(Color color);
/*
    // Find cards by the gamePlayer's id
    List<Card> findByGamePlayerId(Integer gamePlayerId);

    // Visible cards in a game (on-board)
    @Query("SELECT c FROM Card c " +
           "WHERE c.gamePlayer.game.id = :gameId " +
           "AND c.onBoard = true")
    List<Card> findOnBoardByGameId(@Param("gameId") Integer gameId);
*/
}
