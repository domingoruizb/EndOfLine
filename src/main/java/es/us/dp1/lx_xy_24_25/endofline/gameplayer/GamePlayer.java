package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "gameplayers")
public class GamePlayer extends BaseEntity {

    @Min(0)
    @Max(3)
    private Integer energy = 3;

    // TODO: Implement color

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
