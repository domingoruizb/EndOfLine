package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.game.GameRepository;
import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardRepository;
import es.us.dp1.lx_xy_24_25.endofline.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    GamePlayerCardRepository gamePlayerCardRepository;
    GameRepository gameRepository;
    GameService gameService;
    GamePlayerService gamePlayerService;

    @Autowired
    public BoardService (
        GamePlayerCardRepository gamePlayerCardRepository,
        GameRepository gameRepository,
        GameService gameService,
        GamePlayerService gamePlayerService
    ) {
        this.gamePlayerCardRepository = gamePlayerCardRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.gamePlayerService = gamePlayerService;
    }

    public void placeCard (
        GamePlayerCard selectedCard
    ) {
        Integer index = BoardUtils.getIndex(
            selectedCard.getPositionX(),
            selectedCard.getPositionY()
        );

        Game game = selectedCard.getGamePlayer().getGame();
        User player = selectedCard.getGamePlayer().getUser();
        GamePlayer gamePlayer = selectedCard.getGamePlayer();

        GamePlayerCard lastPlacedCard = gamePlayerCardRepository
            .findPlacedCards(selectedCard.getGamePlayer().getId())
            .getFirst();

        Boolean isHost = gameRepository
            .getIsHostOfGame(
                game.getId(),
                player.getId()
            );

        if (!getIsPlacementValid(
            index,
            selectedCard,
            lastPlacedCard,
            selectedCard.getGamePlayer().getId(),
            selectedCard.getGamePlayer().getGame().getId(),
            isHost
        )) {
            throw new IllegalArgumentException("Invalid card placement");
        }

        Integer rotation = BoardUtils.getRotation(index, lastPlacedCard);

        selectedCard.setRotation(rotation);
        gamePlayerCardRepository.save(selectedCard);

        Boolean isTurnFinished = getIsTurnFinished(gamePlayer);

        if (isTurnFinished) {
            gamePlayerService.setCardPlayedThisRoundTo0(gamePlayer);
            gameService.advanceTurn(game);
        } else {
            gamePlayerService.incrementCardsPlayedThisRound(gamePlayer);
        }
    }

    public List<Integer> getPlaceableIndexes (
        GamePlayerCard lastPlacedCard,
        Integer gameId
    ) {
        List<GamePlayerCard> board = gamePlayerCardRepository.findByGameId(gameId);
        return BoardUtils.getValidIndexes(lastPlacedCard, board);
    }

    public Boolean getIsPlacementValid (
        Integer selectedIndex,
        GamePlayerCard selectedCard,
        GamePlayerCard lastPlacedCard,
        Integer gamePlayerId,
        Integer gameId,
        Boolean isHost
    ) {
        List<GamePlayerCard> board = gamePlayerCardRepository.findByGameId(gameId);
        List<GamePlayerCard> lastPlacedCards = gamePlayerCardRepository.findPlacedCards(gamePlayerId);

        if (selectedCard == null) {
            return false;
        }

        if (lastPlacedCards.isEmpty()) {
            if (isHost == null) {
                return false;
            }

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

        if (lastPlacedCard != null) {
            List<Integer> validIndexes = BoardUtils.getValidIndexes(lastPlacedCard, board);

            if (!validIndexes.contains(selectedIndex)) {
                return false;
            }
        }

        return !isStart && BoardUtils.getIsIndexEmpty(selectedIndex, board);
    }

    public Boolean getCanReverse (
        List<GamePlayerCard> lastPlacedCards,
        GamePlayerCard lastPlacedCard,
        List<GamePlayerCard> board
    ) {
        GamePlayerCard reverseCard = getReverseCard(lastPlacedCard, lastPlacedCards);

        if (reverseCard == null) {
            return false;
        }

        List<Integer> validIndexes = BoardUtils.getValidIndexes(reverseCard, board);
        return !validIndexes.isEmpty();
    }

    public GamePlayerCard getReverseCard (GamePlayerCard lastPlacedCard, List<GamePlayerCard> lastPlacedCards) {
        if (lastPlacedCards.size() < 2) {
            return null;
        }

        for (Integer i = lastPlacedCards.size() - 2; i >= 0; i--) {
            GamePlayerCard candidate = lastPlacedCards.get(i);

            if (BoardUtils.getIsConnected(candidate, lastPlacedCard)) {
                return candidate;
            }
        }

        return null;
    }

    private Boolean getIsTurnFinished(GamePlayer gamePlayer) {
        Integer cardLimitInTurn = 2;
        Game game = gamePlayer.getGame();
        Skill skill = game.getSkill();
        if (skill == Skill.BRAKE) {
            cardLimitInTurn = 1;
        } else if (skill == Skill.EXTRA_GAS) {
            cardLimitInTurn = 3;
        }
        return gamePlayer.getCardsPlayedThisRound() + 1 == cardLimitInTurn;
    }
}
