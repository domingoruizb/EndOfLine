package es.us.dp1.lx_xy_24_25.endofline.board.dto;

import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class BoardStateDTO {

    private Integer userId;
    private Integer gameId;
    private Integer playerId;

    private LocalDateTime startedAt;

    private List<Integer> placeable;
    private List<Integer> reversible;

    private Integer energy;

    private Integer turn;
    private Integer round;

    private Skill skill;

    private List<BoardPlayerDTO> players;
    private List<BoardCardDTO> cards;
    public BoardStateDTO(
        Integer userId,
        Integer gameId,
        Integer playerId,
        LocalDateTime startedAt,
        List<Integer> placeable,
        List<Integer> reversible,
        Integer energy,
        Integer turn,
        Integer round,
        Skill skill,
        List<BoardPlayerDTO> players,
        List<BoardCardDTO> cards
    ) {
        this.userId = userId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.startedAt = startedAt;
        this.placeable = placeable;
        this.reversible = reversible;
        this.energy = energy;
        this.turn = turn;
        this.round = round;
        this.skill = skill;
        this.players = players;
        this.cards = cards;
    }

}
