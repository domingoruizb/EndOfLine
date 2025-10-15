package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Game extends BaseEntity {

    // Round 0 if in "Lobby"
    @Min(0)
    private Integer round;

    // Not available if in "Lobby"
    @Column
    private LocalDateTime startedAt;

    // Not available if ongoing game
    @Column
    private LocalDateTime endedAt;

    // TODO: Implement winner

}
