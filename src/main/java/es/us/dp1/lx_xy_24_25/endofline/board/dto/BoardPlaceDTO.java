package es.us.dp1.lx_xy_24_25.endofline.board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPlaceDTO {

    @NotNull
    private Integer cardId;

    @NotNull
    private Integer index;

    public BoardPlaceDTO(
        Integer cardId,
        Integer index
    ) {
        this.cardId = cardId;
        this.index = index;
    }

}
