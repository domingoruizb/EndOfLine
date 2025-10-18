package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private User winner;

    @ManyToOne(optional = false)
    private User host;

    // Method to end a game and declare a winner
    public void endGame(User winner) {
        this.winner = winner;
        this.endedAt = LocalDateTime.now();
    }

    // Method to start a match
    public void startGame() {
        this.startedAt = LocalDateTime.now();
        this.round = 1;
    }
}
