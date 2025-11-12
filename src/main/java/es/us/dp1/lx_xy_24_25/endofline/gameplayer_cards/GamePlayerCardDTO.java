package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GamePlayerCardDTO {

    @JsonProperty("image")
    private String image;

    @JsonProperty("position_x")
    private Integer positionX;

    @JsonProperty("position_y")
    private Integer positionY;

    @JsonProperty("rotation")
    private Integer rotation;

    public GamePlayerCardDTO() {}
    public GamePlayerCardDTO(
        String image,
        Integer positionX,
        Integer positionY,
        Integer rotation
    ) {
        this.image = image;
        this.positionX = positionX;
        this.positionY = positionY;
        this.rotation = rotation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }
}
