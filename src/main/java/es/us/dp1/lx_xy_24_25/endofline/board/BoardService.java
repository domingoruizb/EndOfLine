package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.game.GameRepository;
import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardRepository;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final GamePlayerCardService gamePlayerCardService;
    GamePlayerCardRepository gamePlayerCardRepository;
    GameRepository gameRepository;
    GameService gameService;
    GamePlayerService gamePlayerService;

    @Autowired
    public BoardService (
        GamePlayerCardRepository gamePlayerCardRepository,
        GameRepository gameRepository,
        GameService gameService,
        GamePlayerService gamePlayerService,
        GamePlayerCardService gamePlayerCardService) {
        this.gamePlayerCardRepository = gamePlayerCardRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.gamePlayerService = gamePlayerService;
        this.gamePlayerCardService = gamePlayerCardService;
    }

    public List<GamePlayerCard> getBoard (Integer gameId) {
        return gamePlayerCardRepository.findByGameId(gameId);
    }

    // TODO: Implement further ideas
    public void placeCard (
        GamePlayer gamePlayer,
        Card card,
        Integer index
    ) {
        Game game = gamePlayer.getGame();
        User user = gamePlayer.getUser();

        // Get the reference card to continue from (either reverse card or last placed)
        Boolean isReversing = game.getSkill() == Skill.REVERSE;
        GamePlayerCard referenceCard = isReversing
            ? getReverseCard(gamePlayer)
            : gamePlayerCardService.getLastPlacedCard(gamePlayer);

        if (!getIsPlacementValid(
            index,
            gamePlayer,
            referenceCard
        )) {
            throw new IllegalArgumentException("Invalid card placement");
        }

        Integer rotation = BoardUtils.getRotation(index, referenceCard);
        GamePlayerCard selectedCard = GamePlayerCard.build(
            gamePlayer,
            card,
            index,
            rotation
        );

        gamePlayerCardRepository.save(selectedCard);

        // Clear REVERSE skill after using it (it's a one-time use per activation)
        if (isReversing) {
            gameService.clearSkill(game);
        }

        List<GamePlayerCard> board = getBoard(game.getId());

        // Check if current player has valid moves
        List<Integer> validIndexes = BoardUtils.getValidIndexes(
            selectedCard,
            board
        );

        List<Integer> reversiblePositions = getReversiblePositions(gamePlayer);

        if (validIndexes.isEmpty() && reversiblePositions.isEmpty()) {
            gameService.giveUpOrLose(
                game.getId(),
                user.getId()
            );
        }

        // Check if opponent has valid moves (they might be blocked)
        GamePlayer opponent = gamePlayerService.getOpponent(gamePlayer);

        if (opponent != null) {
            checkOpponent(game, opponent, board);
        }

        gamePlayerService.incrementCardsPlayedThisRound(gamePlayer);

        Boolean isTurnFinished = BoardUtils.getIsTurnFinished(gamePlayer);
        if (isTurnFinished) {
            gameService.advanceTurn(game);
        }
    }

    public void checkOpponent (Game game, GamePlayer opponent, List<GamePlayerCard> board) {
        GamePlayerCard opponentLastCard = gamePlayerCardService.getLastPlacedCard(opponent);

        if (opponentLastCard != null) {
            List<Integer> opponentValidIndexes = BoardUtils.getValidIndexes(opponentLastCard, board);
            List<Integer> opponentReversiblePositions = getReversiblePositions(opponent);

            if (opponentValidIndexes.isEmpty() && opponentReversiblePositions.isEmpty()) {
                gameService.giveUpOrLose(
                    game.getId(),
                    opponent.getUser().getId()
                );
            }
        }
    }

    public Boolean getIsPlacementValid (
        Integer selectedIndex,
        GamePlayer gamePlayer,
        GamePlayerCard referenceCard
    ) {
        List<GamePlayerCard> board = getBoard(gamePlayer.getGame().getId());
        Boolean isHost = gamePlayerService.isHost(gamePlayer);

        // First card placement - check initial valid positions
        if (referenceCard == null) {
            if (!BoardUtils.getInitialValidIndexes(isHost).contains(selectedIndex)) {
                return false;
            }

            return true;
        }

        // Get valid indexes from the reference card (could be last placed or reverse card)
        List<Integer> validIndexes = BoardUtils.getValidIndexes(referenceCard, board);

        if (!validIndexes.contains(selectedIndex)) {
            return false;
        }

        return BoardUtils.getIsIndexEmpty(selectedIndex, board);
    }

    // TODO: Possibly remove
//    public Boolean getCanReverse (
//        GamePlayer gamePlayer
//    ) {
//        List<GamePlayerCard> board = getBoard(gamePlayer.getGame().getId());
//        GamePlayerCard reverseCard = getReverseCard(gamePlayer);
//
//        if (reverseCard == null) {
//            return false;
//        }
//
//        List<Integer> validIndexes = BoardUtils.getValidIndexes(reverseCard, board);
//        return !validIndexes.isEmpty();
//    }

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

    public List<Integer> getReversiblePositions (
        GamePlayer gamePlayer
    ) {
        GamePlayerCard reverseCard = getReverseCard(gamePlayer);

        if (reverseCard == null) {
            return List.of();
        }

        List<GamePlayerCard> board = getBoard(gamePlayer.getGame().getId());

        // If player has no energy left, they can't use REVERSE skill
        return gamePlayer.getEnergy() > 0 ? BoardUtils.getValidIndexes(
            reverseCard,
            board
        ) : List.of();
    }

    public void increaseDeckRequests (
        GamePlayer gamePlayer
    ) {
        Integer deckRequests = gamePlayer.getDeckRequests();
        gamePlayer.setDeckRequests(deckRequests == null ? 1 : deckRequests + 1);
        gamePlayerService.updateGamePlayer(gamePlayer);
    }
}
