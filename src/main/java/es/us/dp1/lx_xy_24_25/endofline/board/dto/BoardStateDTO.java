package es.us.dp1.lx_xy_24_25.endofline.board.dto;

import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerUtils;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class BoardStateDTO {

    private Integer userId;
    private Integer gameId;
    private Integer playerId;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private BoardPlayerDTO winner;

    private List<Integer> placeable;
    private List<Integer> reversible;

    private Integer energy;

    private Integer turn;
    private Integer round;

    private Skill skill;

    private Boolean skillsAvailable;
    private Boolean deckChangeAvailable;

    private List<BoardPlayerDTO> players;
    private List<BoardCardDTO> cards;
    private List<Card> deck;

    private Boolean spectating;

    public BoardStateDTO(
        Integer userId,
        Integer gameId,
        Integer playerId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        BoardPlayerDTO winner,
        List<Integer> placeable,
        List<Integer> reversible,
        Integer energy,
        Integer turn,
        Integer round,
        Skill skill,
        Boolean skillsAvailable,
        Boolean deckChangeAvailable,
        List<BoardPlayerDTO> players,
        List<BoardCardDTO> cards,
        List<Card> deck,
        Boolean spectating
    ) {
        this.userId = userId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.winner = winner;
        this.placeable = placeable;
        this.reversible = reversible;
        this.energy = energy;
        this.turn = turn;
        this.round = round;
        this.skill = skill;
        this.skillsAvailable = skillsAvailable;
        this.deckChangeAvailable = deckChangeAvailable;
        this.players = players;
        this.cards = cards;
        this.deck = deck;
        this.spectating = spectating;
    }

    public static BoardStateDTO build (
        GamePlayer gamePlayer,
        List<GamePlayerCard> board,
        List<Integer> placeable,
        List<Integer> reversible,
        List<Card> deck,
        Boolean spectating
    ) {
        Game game = gamePlayer.getGame();
        GamePlayer winner = GamePlayerUtils.getWinner(game);
        Skill skill = GamePlayerUtils.isValidTurn(gamePlayer) ? game.getSkill() : null;

        return new BoardStateDTO(
            gamePlayer.getUser().getId(),
            game.getId(),
            gamePlayer.getId(),
            game.getStartedAt(),
            game.getEndedAt(),
            winner != null ? BoardPlayerDTO.build(winner) : null,
            // Return reversible positions if REVERSE skill is active, otherwise placeable positions
            spectating ? List.of() : (skill == Skill.REVERSE ? reversible : placeable),
            spectating ? List.of() : reversible,
            gamePlayer.getEnergy(),
            game.getTurn(),
            game.getRound(),
            skill,
            GamePlayerUtils.isSkillAvailable(gamePlayer),
            GamePlayerUtils.isDeckChangeAvailable(gamePlayer),
            game.getGamePlayers().stream().map(BoardPlayerDTO::build).toList(),
            board.stream().map(BoardCardDTO::build).toList(),
            spectating ? List.of() : deck,
            spectating
        );
    }

}
