package es.us.dp1.lIng_04_25_26.endofline.board.dto;

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
