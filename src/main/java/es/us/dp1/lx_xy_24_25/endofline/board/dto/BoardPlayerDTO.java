package es.us.dp1.lx_xy_24_25.endofline.board.dto;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPlayerDTO {

    private Integer playerId;
    private Integer userId;
    private String color;
    private String username;
    private Boolean isHost;

    public BoardPlayerDTO(
        Integer playerId,
        Integer userId,
        String color,
        String username,
        Boolean isHost
    ) {
        this.playerId = playerId;
        this.userId = userId;
        this.color = color;
        this.username = username;
        this.isHost = isHost;
    }

    public static BoardPlayerDTO build (
        GamePlayer gamePlayer
    ) {
        return new BoardPlayerDTO(
            gamePlayer.getId(),
            gamePlayer.getUser().getId(),
            gamePlayer.getColor().toString(),
            gamePlayer.getUser().getUsername(),
            GamePlayerUtils.isHost(gamePlayer)
        );
    }

}
