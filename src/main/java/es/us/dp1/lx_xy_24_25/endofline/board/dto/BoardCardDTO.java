package es.us.dp1.lx_xy_24_25.endofline.board.dto;

import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardCardDTO {

    private Integer playerId;
    private Integer index;
    private Integer rotation;
    private LocalDateTime placedAt;

    private Card card;

    public BoardCardDTO(
        Integer playerId,
        Integer index,
        Integer rotation,
        LocalDateTime placedAt,
        Card card
    ) {
        this.playerId = playerId;
        this.index = index;
        this.rotation = rotation;
        this.placedAt = placedAt;
        this.card = card;
    }

}
