package es.us.dp1.lx_xy_24_25.endofline.board;

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

    public GamePlayerCard increasePlacedCards (
        GamePlayerCard gamePlayerCard,
        Boolean isTurnFinished
    ) {
        GamePlayerCard saved = gamePlayerCardRepository.save(gamePlayerCard);

        gamePlayerService.incrementCardsPlayedThisRound(gamePlayerCard.getGamePlayer());

        if (isTurnFinished) {
            gameService.advanceTurn(gamePlayerCard.getGamePlayer().getGame());
        }

        return saved;
    }

    // TODO: Implement
    public GamePlayerCard placeCard (
        GamePlayerCard selectedCard
    ) {
        Integer index = BoardUtils.getIndex(
            selectedCard.getPositionX(),
            selectedCard.getPositionY()
        );

        GamePlayer gamePlayer = selectedCard.getGamePlayer();
        Game game = gamePlayer.getGame();
        User user = gamePlayer.getUser();

        GamePlayerCard lastPlacedCard = gamePlayerCardService.getLastPlacedCard(gamePlayer);

        if (!getIsPlacementValid(
            index,
            selectedCard,
            gamePlayer,
            game
        )) {
            throw new IllegalArgumentException("Invalid card placement");
        }

        Integer rotation = BoardUtils.getRotation(index, lastPlacedCard);

        selectedCard.setRotation(rotation);

        gamePlayerCardRepository.save(selectedCard);

//        Boolean isTurnFinished = BoardUtils.getIsTurnFinished(gamePlayer);
//
//        if (isTurnFinished) {
//            gamePlayerService.setCardPlayedThisRoundTo0(gamePlayer);
//            gameService.advanceTurn(game);
//        } else {
//            gamePlayerService.incrementCardsPlayedThisRound(gamePlayer);
//        }

//        List<GamePlayerCard> board = getBoard(game.getId());
//        List<Integer> validIndexes = BoardUtils.getValidIndexes(
//            selectedCard,
//            board
//        );

//        if (validIndexes.isEmpty()) {
//            gameService.giveUpOrLose(
//                game.getId(),
//                user.getId()
//            );
//        }

//        GamePlayerCard saved = increasePlacedCards(selectedCard, isTurnFinished);

//        return saved;
        return null;
    }

    public Boolean getIsPlacementValid (
        Integer selectedIndex,
        GamePlayerCard selectedCard,
        GamePlayer gamePlayer,
        Game game
    ) {
        List<GamePlayerCard> board = getBoard(game.getId());
        List<GamePlayerCard> lastPlacedCards = gamePlayerCardService.getLastPlacedCards(gamePlayer);
        Boolean isHost = gamePlayerService.isHost(gamePlayer);

        if (selectedCard == null) {
            return false;
        }

        if (lastPlacedCards.isEmpty()) {
            if (!BoardUtils.getInitialValidIndexes(isHost).contains(selectedIndex)) {
                return false;
            }
        }

        BoardPosition coordinates = BoardUtils.getCoordinates(selectedIndex);
        Boolean isStart = BoardUtils.INITIAL_POSITIONS
            .stream()
            .anyMatch(pos ->
                pos.getFirst().equals(coordinates.row()) &&
                pos.getLast().equals(coordinates.col())
            );

        if (game.getSkill().equals(Skill.REVERSE)) {
            List<Integer> reversiblePositions = getReversiblePositions(gamePlayer);
            if (reversiblePositions.contains(selectedIndex)) {
                return !isStart && BoardUtils.getIsIndexEmpty(selectedIndex, board);
            }
        }

        GamePlayerCard lastPlacedCard = lastPlacedCards.getFirst();
        List<Integer> validIndexes = BoardUtils.getValidIndexes(lastPlacedCard, board);

        if (!validIndexes.contains(selectedIndex)) {
            return false;
        }

        return !isStart && BoardUtils.getIsIndexEmpty(selectedIndex, board);
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

        for (Integer i = lastPlacedCards.size() - 2; i >= 0; i--) {
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

        return BoardUtils.getValidIndexes(
            reverseCard,
            board
        );
    }
}
