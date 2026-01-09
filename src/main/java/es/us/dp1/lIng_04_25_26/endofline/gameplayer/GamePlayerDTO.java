package es.us.dp1.lIng_04_25_26.endofline.gameplayer;

import es.us.dp1.lIng_04_25_26.endofline.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GamePlayerDTO {
    private Integer id;
    private Integer energy;
    private String color;
    private Integer cardsPlayedThisRound;
    private Boolean canRequestDeck;
    private List<Integer> deckCards;
    private UserDTO user;

    public GamePlayerDTO(GamePlayer gamePlayer) {
        this.id = gamePlayer.getId();
        this.energy = gamePlayer.getEnergy();
        this.color = gamePlayer.getColor() != null ? gamePlayer.getColor().name() : null;
        this.cardsPlayedThisRound = gamePlayer.getCardsPlayedThisRound();
        this.canRequestDeck = gamePlayer.getCanRequestDeck();
        this.deckCards = gamePlayer.getDeckCards();
        this.user = gamePlayer.getUser() != null ? new UserDTO(gamePlayer.getUser()) : null;
    }
}
