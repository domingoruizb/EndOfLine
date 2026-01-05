package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Game extends BaseEntity {

    @Min(0)
    private Integer round = 0;

    @Column(length = 6, unique = true)
    private String code;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @ManyToOne
    private User winner;

    @ManyToOne(optional = false)
    private User host;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamePlayer> gamePlayers;

    @JoinColumn(name = "turn")
    private Integer turn;

    private Skill skill;

    public void markAsStarted() {
        this.startedAt = LocalDateTime.now();
        this.round = 1;
    }

    public void markAsEnded(User winner) {
        this.winner = winner;
        this.endedAt = LocalDateTime.now();
    }

    public static Game build (
        String code,
        User host
    ) {
        Game game = new Game();
        game.setRound(0);
        game.setCode(code);
        game.setHost(host);
        game.setStartedAt(LocalDateTime.now());

        GamePlayer hostGamePlayer = GamePlayer.build(game, host);

        game.setGamePlayers(new ArrayList<>());
        game.getGamePlayers().add(hostGamePlayer);

        return game;
    }
}
