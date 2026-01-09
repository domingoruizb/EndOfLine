package es.us.dp1.lIng_04_25_26.endofline.gameplayer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.model.BaseEntity;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@Setter
@Table(name = "game_players")
public class GamePlayer extends BaseEntity {

    @Min(0)
    @Max(3)
    private Integer energy = 3;

    private Color color;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Min(0)
    private Integer cardsPlayedThisRound = 0;

    private Boolean canRequestDeck = true;

    @ElementCollection
    @CollectionTable(name = "game_player_skills", joinColumns = @JoinColumn(name = "game_player_id"))
    private List<SkillUsage> skillsUsed = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_player_deck_cards", joinColumns = @JoinColumn(name = "game_player_id"))
    private List<Integer> deckCards = new ArrayList<>();

    public static GamePlayer build(
        Game game,
        User user
    ) {
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGame(game);
        gamePlayer.setUser(user);
        return gamePlayer;
    }
}
