package es.us.dp1.lIng_04_25_26.endofline.gameplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.gameplayer.GamePlayerNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Game Module")
@Feature("GamePlayer Management")
@Owner("DP1-tutors")
@SpringBootTest
class GamePlayerServiceTests {

    @Autowired
    private GamePlayerService gamePlayerService;

    @MockBean
    private GamePlayerRepository gamePlayerRepository;

    private GamePlayer gp;
    private Game game;
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(20);
        user.setUsername("TestUser");

        game = new Game();
        game.setId(10);
        game.setTurn(20);

        gp = new GamePlayer();
        gp.setId(1);
        gp.setUser(user);
        gp.setGame(game);
        gp.setEnergy(3);
        gp.setColor(Color.RED);
        gp.setCardsPlayedThisRound(0);
        gp.setCanRequestDeck(true);
    }


    @Test
    void testFindGamePlayerById() {
        when(gamePlayerRepository.findById(1)).thenReturn(Optional.of(gp));
        GamePlayer found = gamePlayerService.getById(1);
        assertEquals(gp.getId(), found.getId());
        assertEquals(Color.RED, found.getColor());
    }


    @Test
    void testFailGetById_NotFound() {
        when(gamePlayerRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(GamePlayerNotFoundException.class, () -> gamePlayerService.getById(99));
    }


    @Test
    void testFindGamePlayerByGameAndUser() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.of(gp));
        GamePlayer found = gamePlayerService.getGamePlayer(10, 20);
        assertEquals(1, found.getId());
    }


    @Test
    void testFailFindGamePlayerByGameAndUser_NotFound() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.empty());
        assertThrows(GamePlayerNotFoundException.class, () -> gamePlayerService.getGamePlayer(10, 20));
    }


    @Test
    void testUpdatePlayerColor() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.of(gp));
        when(gamePlayerRepository.save(any(GamePlayer.class))).thenAnswer(i -> i.getArguments()[0]);

        GamePlayer result = gamePlayerService.updatePlayerColor(10, 20, "\"BLUE\"");
        
        assertEquals(Color.BLUE, result.getColor());
        verify(gamePlayerRepository).save(gp);
    }


    @Test
    void testFailUpdateColor_InvalidColor() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.of(gp));
        assertThrows(IllegalArgumentException.class, 
            () -> gamePlayerService.updatePlayerColor(10, 20, "INVALID_COLOR"));
    }


    @Test
    void testFailUpdateColor_GamePlayerNotFound() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.empty());
        assertThrows(GamePlayerNotFoundException.class, 
            () -> gamePlayerService.updatePlayerColor(10, 20, "GREEN"));
    }


    @Test
    void testIdentifyAsSpectator() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.empty());
        assertTrue(gamePlayerService.isSpectating(game, user));
    }


    @Test
    void testIdentifyAsPlayer() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.of(gp));
        assertFalse(gamePlayerService.isSpectating(game, user));
    }


    @Test
    void testGetOpponent() {
        GamePlayer opponent = new GamePlayer();
        opponent.setId(2);
        when(gamePlayerRepository.findOpponent(10, 1)).thenReturn(Optional.of(opponent));

        GamePlayer found = gamePlayerService.getOpponent(gp);
        assertEquals(2, found.getId());
    }


    @Test
    void testFailGetOpponent_NotFound() {
        when(gamePlayerRepository.findOpponent(10, 1)).thenReturn(Optional.empty());
        assertThrows(GamePlayerNotFoundException.class, () -> gamePlayerService.getOpponent(gp));
    }


    @Test
    void testGetNextPlayer() {
        GamePlayer nextPlayer = new GamePlayer();
        nextPlayer.setId(3);
        when(gamePlayerRepository.findNextPlayer(10, 20)).thenReturn(Optional.of(nextPlayer));

        GamePlayer found = gamePlayerService.getNextPlayer(game);
        assertEquals(3, found.getId());
    }


    @Test
    void testIncrementCardsPlayed() {
        gp.setCardsPlayedThisRound(2);
        gamePlayerService.incrementCardsPlayedThisRound(gp);
        assertEquals(3, gp.getCardsPlayedThisRound());
    }


    @Test
    void testResetCardsPlayed() {
        gp.setCardsPlayedThisRound(5);
        gamePlayerService.resetCardsPlayedThisRound(gp);
        assertEquals(0, gp.getCardsPlayedThisRound());
    }


    @Test
    void testUpdateGamePlayer() {
        when(gamePlayerRepository.save(gp)).thenReturn(gp);
        gamePlayerService.updateGamePlayer(gp);
        verify(gamePlayerRepository).save(gp);
    }


    @Test
    void testGetGamePlayer_WhenUserIsPlayer() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.of(gp));
        
        GamePlayer result = gamePlayerService.getGamePlayerOrFriend(game, user);
        
        assertEquals(gp, result);
        verify(gamePlayerRepository, org.mockito.Mockito.never()).findFriendsInGame(any(), any());
    }


    @Test
    void testGetFriend_WhenUserIsSpectatorButHasFriend() {
        GamePlayer friendGp = new GamePlayer();
        friendGp.setId(5);
        
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.empty());
        when(gamePlayerRepository.findFriendsInGame(20, 10)).thenReturn(List.of(friendGp));
        
        GamePlayer result = gamePlayerService.getGamePlayerOrFriend(game, user);
        
        assertEquals(friendGp, result);
    }


    @Test
    void testFailGetGamePlayerOrFriend_WhenNeitherExists() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20)).thenReturn(Optional.empty());
        when(gamePlayerRepository.findFriendsInGame(20, 10)).thenReturn(Collections.emptyList());
        
        assertThrows(GamePlayerNotFoundException.class, 
            () -> gamePlayerService.getGamePlayerOrFriend(game, user));
    }


    @Test
    void testGetFriendInGame() {
        GamePlayer friendGp = new GamePlayer();
        friendGp.setId(99);
        when(gamePlayerRepository.findFriendsInGame(20, 10)).thenReturn(List.of(friendGp));
        
        GamePlayer result = gamePlayerService.getFriendInGame(user, game);
        assertEquals(99, result.getId());
    }


    @Test
    void testFailGetFriendInGame_NotFound() {
        when(gamePlayerRepository.findFriendsInGame(20, 10)).thenReturn(Collections.emptyList());
        assertThrows(GamePlayerNotFoundException.class, () -> gamePlayerService.getFriendInGame(user, game));
    }

}