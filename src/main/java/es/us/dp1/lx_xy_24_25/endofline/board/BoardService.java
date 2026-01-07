package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.board.dto.BoardStateDTO;
import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.card.CardService;
import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.card.CardForbiddenException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.game.TurnForbiddenException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.gameplayer.GamePlayerNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerUtils;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private final GamePlayerCardService gamePlayerCardService;
    private final GameService gameService;
    private final GamePlayerService gamePlayerService;
    private final CardService cardService;

    @Autowired
    public BoardService (
        GameService gameService,
        GamePlayerService gamePlayerService,
        GamePlayerCardService gamePlayerCardService,
        CardService cardService
    ) {
        this.gameService = gameService;
        this.gamePlayerService = gamePlayerService;
        this.gamePlayerCardService = gamePlayerCardService;
        this.cardService = cardService;
    }

    @Transactional(readOnly = true)
    public void checkOpponent (GamePlayer opponent, List<GamePlayerCard> board) {
        GamePlayerCard opponentLastCard = gamePlayerCardService.getLastPlacedCard(opponent);

        if (opponentLastCard != null) {
            List<Integer> opponentValidIndexes = BoardUtils.getValidIndexes(opponentLastCard, board);
            List<Integer> opponentReversiblePositions = getReversiblePositions(opponent);

            if (opponentValidIndexes.isEmpty() && opponentReversiblePositions.isEmpty()) {
                gameService.giveUpOrLose(opponent);
            }
        }
    }

    @Transactional(readOnly = true)
    public Boolean getIsPlacementValid (
        Integer selectedIndex,
        GamePlayer gamePlayer,
        GamePlayerCard referenceCard
    ) {
        List<GamePlayerCard> board = gamePlayerCardService.getByGame(gamePlayer.getGame());
        Boolean isHost = GamePlayerUtils.isHost(gamePlayer);

        // First card placement - check initial valid positions
        if (referenceCard == null) {
            return BoardUtils.getInitialValidIndexes(isHost).contains(selectedIndex);
        }

        // Get valid indexes from the reference card (could be last placed or reverse card)
        List<Integer> validIndexes = BoardUtils.getValidIndexes(referenceCard, board);

        if (!validIndexes.contains(selectedIndex)) {
            return false;
        }

        return BoardUtils.getIsIndexEmpty(selectedIndex, board);
    }

    @Transactional(readOnly = true)
    public GamePlayerCard getReverseCard (GamePlayer gamePlayer) {
        List<GamePlayerCard> lastPlacedCards = gamePlayerCardService.getLastPlacedCards(gamePlayer);

        if (lastPlacedCards.size() < 2) {
            return null;
        }

        GamePlayerCard lastPlacedCard = lastPlacedCards.getFirst();

        for (Integer i = 1; i < lastPlacedCards.size(); i++) {
            GamePlayerCard candidate = lastPlacedCards.get(i);

            if (BoardUtils.getIsConnected(candidate, lastPlacedCard)) {
                return candidate;
            }
        }

        return null;
    }

    @Transactional(readOnly = true)
    public List<Integer> getReversiblePositions (
        GamePlayer gamePlayer
    ) {
        GamePlayerCard reverseCard = getReverseCard(gamePlayer);

        if (reverseCard == null) {
            return List.of();
        }

        List<GamePlayerCard> board = gamePlayerCardService.getByGame(gamePlayer.getGame());
        Game game = gamePlayer.getGame();

        // If REVERSE skill is active, allow reversal regardless of energy
        if (game.getSkill() == Skill.REVERSE) {
            return BoardUtils.getValidIndexes(reverseCard, board);
        }

        // Otherwise, require energy > 0
        return gamePlayer.getEnergy() > 0 ? BoardUtils.getValidIndexes(reverseCard, board) : List.of();
    }

    @Transactional
    public void placeCard (
        GamePlayer gamePlayer,
        Card card,
        Integer index
    ) {
        if (!GamePlayerUtils.isValidTurn(gamePlayer)) {
            throw new TurnForbiddenException();
        }

        Game game = gamePlayer.getGame();

        // Get the reference card to continue from (either reverse card or last placed)
        Boolean isReversing = game.getSkill() == Skill.REVERSE;
        GamePlayerCard referenceCard = isReversing ? getReverseCard(gamePlayer) : gamePlayerCardService.getLastPlacedCard(gamePlayer);

        if (!getIsPlacementValid(index, gamePlayer, referenceCard)) {
            throw new CardForbiddenException();
        }

        Integer rotation = BoardUtils.getRotation(index, referenceCard);
        GamePlayerCard selectedCard = GamePlayerCard.build(gamePlayer, card, index, rotation);

        cardService.removeFromDeck(gamePlayer, card);
        gamePlayerCardService.save(selectedCard);

        // Clear REVERSE skill after using it (it's a one-time use per activation)
        if (isReversing) {
            game.setSkill(null);
        }

        List<GamePlayerCard> board = gamePlayerCardService.getByGame(game);

        List<Integer> reversiblePositions = getReversiblePositions(gamePlayer);
        List<Integer> validIndexes = BoardUtils.getValidIndexes(selectedCard, board);

        if (validIndexes.isEmpty() && reversiblePositions.isEmpty()) {
            gameService.giveUpOrLose(gamePlayer);
        }

        // Check if opponent has valid moves (they might be blocked)
        GamePlayer opponent = gamePlayerService.getOpponent(gamePlayer);
        checkOpponent(opponent, board);

        gamePlayerService.incrementCardsPlayedThisRound(gamePlayer);

        Boolean isTurnFinished = BoardUtils.getIsTurnFinished(gamePlayer);
        if (isTurnFinished) {
            gameService.advanceTurn(game);
        }
    }

    @Transactional
    public BoardStateDTO getState (
        GamePlayer gamePlayer,
        Game game,
        Boolean isSpectating
    ) {
        if (gamePlayer == null) {
            throw new GamePlayerNotFoundException();
        }

        GamePlayerCard lastPlacedCard = gamePlayerCardService.getLastPlacedCard(gamePlayer);

        List<GamePlayerCard> board = gamePlayerCardService.getByGame(game);

        Boolean isTurn = GamePlayerUtils.isValidTurn(gamePlayer);
        Boolean isHost = GamePlayerUtils.isHost(gamePlayer);

        // Don't return placeable positions if it's not the player's turn
        // If it's the first turn, return initial valid positions
        // Otherwise, return valid positions based on the last placed card
        List<Integer> placeablePositions = isTurn ? (
            lastPlacedCard != null ? BoardUtils.getValidIndexes(lastPlacedCard, board) : BoardUtils.getInitialValidIndexes(isHost)
        ) : List.of();

        // Only return reversible positions if it's the player's turn
        List<Integer> reversiblePositions = isTurn && lastPlacedCard != null ? getReversiblePositions(gamePlayer) : List.of();
        List<Card> deck = cardService.getDeckCards(gamePlayer);

        return BoardStateDTO.build(
            gamePlayer,
            board,
            placeablePositions,
            reversiblePositions,
            deck,
            isSpectating
        );
    }
}
