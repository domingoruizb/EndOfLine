package es.us.dp1.lIng_04_25_26.endofline.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import es.us.dp1.lIng_04_25_26.endofline.board.BoardUtils;
import es.us.dp1.lIng_04_25_26.endofline.card.Card;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCard;

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

    public static BoardCardDTO build (
        GamePlayerCard gamePlayerCard
    ) {
        return new BoardCardDTO(
            gamePlayerCard.getGamePlayer().getId(),
            BoardUtils.getIndex(gamePlayerCard.getPositionX(), gamePlayerCard.getPositionY()),
            gamePlayerCard.getRotation(),
            gamePlayerCard.getPlacedAt(),
            gamePlayerCard.getCard()
        );
    }

}
