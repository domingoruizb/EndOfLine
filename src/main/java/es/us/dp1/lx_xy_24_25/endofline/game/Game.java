package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.smartcardio.Card;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Game extends BaseEntity {

    // Round 0 if in "Lobby"
    @Min(0)
    private Integer round;

    // Not available if in "Lobby"
    @Column
    private LocalDateTime startedAt;

    // Not available if ongoing game
    @Column
    private LocalDateTime endedAt;

    @ManyToOne
    private User winner;

    @ManyToOne(optional = false)
    private User host;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamePlayer> gamePlayers;

    @ManyToOne
    @JoinColumn(name = "turn_user_id")
    private Integer turn;

    // Method to start a game and declare a winner
    public void startGame() {
        this.startedAt = LocalDateTime.now();
        this.round = 1;
        this.turn = determineFirstTurn();
    }

    // Method to end a match
    public void endGame(User winner) {
        this.winner = winner;
        this.endedAt = LocalDateTime.now();
    }

    
    // Method to move to next round
    public void nextRound() {
        if (this.round == null) {
            this.round = 1;
        } else {
            this.round++;
        }
        this.turn = determineFirstTurn();
    }

    // Determine the player who drafted the card with the lowest initiative
    private Integer determineFirstTurn() {
        if (gamePlayers == null || gamePlayers.isEmpty()) {
            throw new IllegalStateException("There are no players in the match.");
        }

        Optional<GamePlayer> firstPlayer = gamePlayers.stream()
                .min(Comparator.comparingInt(this::getInitiativeOfMostRecentCard));

        return firstPlayer.map(x -> x.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("First turn couldn't be decided."));
    }

    
    public void nextTurn() {
        if (gamePlayers == null || gamePlayers.isEmpty()) {
            throw new IllegalStateException("There are no players in the match.");
        }

        // Order players by lowest initiative
        List<GamePlayer> orderedPlayers = gamePlayers.stream()
                .sorted(Comparator.comparingInt(this::getInitiativeOfMostRecentCard))
                .toList();

        int currentIndex = -1;
        for (int i = 0; i < orderedPlayers.size(); i++) {
            if (orderedPlayers.get(i).getUser().getId().equals(this.turn)) {
                currentIndex = i;
                break;
            }
        }

        // Next turn by initiative (circular)
        int nextIndex = (currentIndex + 1) % orderedPlayers.size();
        this.turn = orderedPlayers.get(nextIndex).getUser().getId();
    }

    
    // Obtains the initiative of the most recently updated card of the player.
    // If he has no cards, returns a high value to leave him at the end.
    private Integer getInitiativeOfMostRecentCard(GamePlayer player) {
        List<Card> cards = player.getCards();
        if (cards == null || cards.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        Optional<Card> mostRecentCard = cards.stream()
                .filter(c -> c.getUpdatedAt() != null)
                .max(Comparator.comparing(Card::getUpdatedAt));

        // If there is no date, we return a high value
        return mostRecentCard.map(Card::getInitiative).orElse(Integer.MAX_VALUE);
    }
}
