package es.us.dp1.lx_xy_24_25.endofline.board.dto;

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

}
