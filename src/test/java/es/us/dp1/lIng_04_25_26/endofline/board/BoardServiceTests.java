package es.us.dp1.lIng_04_25_26.endofline.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.board.dto.BoardStateDTO;
import es.us.dp1.lIng_04_25_26.endofline.card.Card;
import es.us.dp1.lIng_04_25_26.endofline.card.CardService;
import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.card.CardForbiddenException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.game.TurnForbiddenException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.game.GameService;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerUtils;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCardService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTests {

    @Mock
    private GameService gameService;

    @Mock
    private GamePlayerService gamePlayerService;

    @Mock
    private GamePlayerCardService gamePlayerCardService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private BoardService boardService;

    private Game game;
    private User user;
    private GamePlayer gamePlayer;
    private GamePlayer opponent;
    private Card card;
    
    private MockedStatic<GamePlayerUtils> gamePlayerUtilsMock;

    @BeforeEach
    void setUp() {
        gamePlayerUtilsMock = mockStatic(GamePlayerUtils.class);

        game = new Game();
        game.setId(1);
        game.setStartedAt(LocalDateTime.now());
        game.setRound(1);
        game.setTurn(1);
        game.setGamePlayers(new ArrayList<>());

        user = new User();
        user.setId(100);
        user.setUsername("Player1");

        User userOpponent = new User();
        userOpponent.setId(101);
        userOpponent.setUsername("Player2");

        gamePlayer = new GamePlayer();
        gamePlayer.setId(1);
        gamePlayer.setUser(user);
        gamePlayer.setGame(game);
        gamePlayer.setEnergy(3);
        gamePlayer.setCardsPlayedThisRound(0);
        gamePlayer.setColor(Color.RED);

        opponent = new GamePlayer();
        opponent.setId(2);
        opponent.setUser(userOpponent);
        opponent.setGame(game);
        opponent.setColor(Color.BLUE);
        
        game.getGamePlayers().add(gamePlayer);
        game.getGamePlayers().add(opponent);

        card = new Card();
        card.setId(50);
        card.setImage("/cards/red_1010.png");
    }

    @AfterEach
    void tearDown() {
        gamePlayerUtilsMock.close();
    }


    @Test
    void testIsPlacementValidFirstTurnHostSuccess() {
        Integer validIndex = 23;
        
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isHost(gamePlayer)).thenReturn(true);
        when(gamePlayerCardService.getByGame(game)).thenReturn(new ArrayList<>());

        Boolean result = boardService.isPlacementValid(validIndex, gamePlayer, null);

        assertTrue(result, "El host debería poder colocar en la posición inicial (2,3) -> index 23");
    }


    @Test
    void testIsPlacementValidOccupiedPositionFail() {
        Integer targetIndex = 23;
        List<GamePlayerCard> board = new ArrayList<>();
        
        GamePlayerCard existingCard = new GamePlayerCard();
        existingCard.setPositionX(2);
        existingCard.setPositionY(3);
        board.add(existingCard);

        GamePlayerCard referenceCard = new GamePlayerCard();
        referenceCard.setId(99);

        try (MockedStatic<BoardUtils> boardUtilsMock = mockStatic(BoardUtils.class)) {
            gamePlayerUtilsMock.when(() -> GamePlayerUtils.isHost(gamePlayer)).thenReturn(true);
            
            boardUtilsMock.when(() -> BoardUtils.getValidIndexes(eq(referenceCard), any()))
                        .thenReturn(List.of(targetIndex));

            boardUtilsMock.when(() -> BoardUtils.getIsIndexEmpty(eq(targetIndex), any()))
                        .thenReturn(false);

            when(gamePlayerCardService.getByGame(game)).thenReturn(board);

            Boolean result = boardService.isPlacementValid(targetIndex, gamePlayer, referenceCard);

            assertFalse(result, "Debe retornar false porque BoardUtils.getIsIndexEmpty devuelve false (casilla ocupada)");
        }
    }


    @Test
    void testPlaceCardSuccessFirstMove() throws Exception {
        Integer validIndex = 23;
        
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isValidTurn(gamePlayer)).thenReturn(true);
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isHost(gamePlayer)).thenReturn(true);
        
        when(gamePlayerCardService.getLastPlacedCard(gamePlayer)).thenReturn(null);
        when(gamePlayerCardService.getByGame(game)).thenReturn(new ArrayList<>());
        when(gamePlayerService.getOpponent(gamePlayer)).thenReturn(opponent);
        when(gamePlayerCardService.getLastPlacedCard(opponent)).thenReturn(null);

        boardService.placeCard(gamePlayer, card, validIndex);

        verify(cardService).removeFromDeck(gamePlayer, card);
        verify(gamePlayerCardService).save(any(GamePlayerCard.class));
        verify(gamePlayerService).incrementCardsPlayedThisRound(gamePlayer);

        gamePlayer.setCardsPlayedThisRound(1); 
    }


    @Test
    void testPlaceCardTurnForbiddenException() {
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isValidTurn(gamePlayer)).thenReturn(false);

        assertThrows(TurnForbiddenException.class, () -> {
            boardService.placeCard(gamePlayer, card, 0);
        });
    }


    @Test
    void testPlaceCardCardForbiddenExceptionInvalidPosition() {
        Integer invalidIndex = 99;

        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isValidTurn(gamePlayer)).thenReturn(true);
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isHost(gamePlayer)).thenReturn(true);
        when(gamePlayerCardService.getLastPlacedCard(gamePlayer)).thenReturn(null);
        when(gamePlayerCardService.getByGame(game)).thenReturn(new ArrayList<>());

        assertThrows(CardForbiddenException.class, () -> {
            boardService.placeCard(gamePlayer, card, invalidIndex);
        });
    }


    @Test
    void testPlaceCardWithReverseSkillSuccess() throws Exception {
        game.setSkill(Skill.REVERSE);
        Integer validIndex = 23;

        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isValidTurn(gamePlayer)).thenReturn(true);
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isHost(gamePlayer)).thenReturn(true);

        when(gamePlayerCardService.getByGame(game)).thenReturn(new ArrayList<>());
        when(gamePlayerService.getOpponent(gamePlayer)).thenReturn(opponent);
        
        boardService.placeCard(gamePlayer, card, validIndex);

        verify(gamePlayerCardService).save(any(GamePlayerCard.class));
        assertEquals(null, game.getSkill(), "La habilidad REVERSE debería consumirse tras usarla");
    }


    @Test
    void testCheckOpponentOpponentHasNoMovesShouldLose() {
        GamePlayerCard opponentLastCard = new GamePlayerCard();
        opponentLastCard.setGamePlayer(opponent);
        opponentLastCard.setCard(card);
        opponentLastCard.setPositionX(0);
        opponentLastCard.setPositionY(0);
        opponentLastCard.setRotation(0);

        List<GamePlayerCard> board = new ArrayList<>();
        board.add(opponentLastCard);

        when(gamePlayerCardService.getLastPlacedCard(opponent)).thenReturn(opponentLastCard);

        opponent.setEnergy(0);

        board.add(createDummyCard(0, 1));
        board.add(createDummyCard(1, 0));
        board.add(createDummyCard(0, 6));
        board.add(createDummyCard(6, 0));

        boardService.checkOpponent(opponent, board);

        verify(gameService).giveUpOrLose(opponent);
    }
    
    private GamePlayerCard createDummyCard(int x, int y) {
        GamePlayerCard c = new GamePlayerCard();
        c.setPositionX(x);
        c.setPositionY(y);
        return c;
    }


    @Test
    void testGetStateSuccess() {
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isValidTurn(gamePlayer)).thenReturn(true);
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isHost(gamePlayer)).thenReturn(true);
        
        when(gamePlayerCardService.getLastPlacedCard(gamePlayer)).thenReturn(null);
        when(gamePlayerCardService.getByGame(game)).thenReturn(new ArrayList<>());
        when(cardService.getDeckCards(gamePlayer)).thenReturn(new ArrayList<>());

        BoardStateDTO result = boardService.getState(gamePlayer, game, false);

        assertNotNull(result);
        assertEquals(user.getId(), result.getUserId());
        assertEquals(game.getId(), result.getGameId());
        assertFalse(result.getSpectating());
    }


    @Test
    void testGetStateSpectator() {
        when(gamePlayerCardService.getLastPlacedCard(gamePlayer)).thenReturn(null);
        when(gamePlayerCardService.getByGame(game)).thenReturn(new ArrayList<>());
        when(cardService.getDeckCards(gamePlayer)).thenReturn(new ArrayList<>());
        
        BoardStateDTO result = boardService.getState(gamePlayer, game, true);
        
        assertTrue(result.getSpectating());
        assertTrue(result.getPlaceable().isEmpty());
    }

}