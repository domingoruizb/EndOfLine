package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import java.time.LocalDateTime;

import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gameplayer_cards")
@Getter
@Setter
public class GamePlayerCard extends BaseEntity {

    @NotNull
    @Min(0)
    @Max(6)
    private Integer positionX;

    @NotNull
    @Min(0)
    @Max(6)
    private Integer positionY;

    @NotNull
    @Min(-3)
    @Max(0)
    private Integer rotation;

    @NotNull
    private LocalDateTime placedAt;

    @ManyToOne
    @JoinColumn(name = "gameplayer_id")
    private GamePlayer gamePlayer;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;
}
