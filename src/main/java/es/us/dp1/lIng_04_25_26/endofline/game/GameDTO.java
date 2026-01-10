package es.us.dp1.lIng_04_25_26.endofline.game;

import es.us.dp1.lIng_04_25_26.endofline.user.UserDTO;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GameDTO {
    private Integer id;
    private Integer round;
    private String code;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private UserDTO winner;
    private UserDTO host;
    private List<Integer> gamePlayerIds;
    private List<GamePlayerDTO> gamePlayers;
    private Integer turn;
    private String skill;

    public GameDTO(Game game) {
        this.id = game.getId();
        this.round = game.getRound();
        this.code = game.getCode();
        this.startedAt = game.getStartedAt();
        this.endedAt = game.getEndedAt();
        this.winner = game.getWinner() != null ? new UserDTO(game.getWinner()) : null;
        this.host = game.getHost() != null ? new UserDTO(game.getHost()) : null;
        this.gamePlayerIds = game.getGamePlayers() != null ? game.getGamePlayers().stream().map(gp -> gp.getId()).toList() : null;
        this.gamePlayers = game.getGamePlayers() != null ? game.getGamePlayers().stream().map(GamePlayerDTO::new).toList() : List.of();
        this.turn = game.getTurn();
        this.skill = game.getSkill() != null ? game.getSkill().name() : null;
    }
}
