package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;


public class GamePlayerCardDTO {

    @JsonProperty("id")
    private Integer id;
    
    @NotNull
    @JsonProperty("card_id")
    private Integer cardId;
    
    @NotNull
    @JsonProperty("card_name")
    private String cardName;

    @NotNull
    @JsonProperty("initiative")
    private Integer initiative;

    @NotNull
    @JsonProperty("position_x")
    private Integer positionX;

    @NotNull
    @JsonProperty("position_y")
    private Integer positionY;

    @NotNull
    @JsonProperty("on_board")
    private Boolean onBoard;

    @NotNull
    @JsonProperty("placed_at")
    private LocalDateTime placedAt;

    // Constructor vacío
    public GamePlayerCardDTO() {}

    
    public GamePlayerCardDTO(Integer id, Integer cardId, String cardName, Integer initiative,
                             Integer positionX, Integer positionY,
                             Boolean onBoard, LocalDateTime placedAt) {
        this.id = id;
        this.cardId = cardId;
        this.cardName = cardName;
        this.initiative = initiative;
        this.positionX = positionX;
        this.positionY = positionY;
        this.onBoard = onBoard;
        this.placedAt = placedAt;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getInitiative() {
        return initiative;
    }

    public void setInitiative(Integer initiative) {
        this.initiative = initiative;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public Boolean getOnBoard() {
        return onBoard;
    }

    public void setOnBoard(Boolean onBoard) {
        this.onBoard = onBoard;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
    }
}
