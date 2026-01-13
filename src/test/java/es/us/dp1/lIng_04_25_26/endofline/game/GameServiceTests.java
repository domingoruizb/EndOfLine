package es.us.dp1.lIng_04_25_26.endofline.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.achievement.AchievementService;
import es.us.dp1.lIng_04_25_26.endofline.card.CardService;
import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.game.GameBadRequestException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.game.GameNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.skill.SkillBadRequestException;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCardService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {

    @Mock
    private GameRepository gameRepository;
    
    @Mock
    private UserService userService;
    
    @Mock
    private GamePlayerService gamePlayerService;
    
    @Mock
    private GamePlayerCardService gamePlayerCardService;
    
    @Mock
    private CardService cardService;
    
    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private GameService gameService;

    private User host;
    private User player2;
    private Game game;
    private GamePlayer gpHost;
    private GamePlayer gpPlayer2;

    @BeforeEach
    void setUp() {
        host = new User();
        host.setId(1);
        host.setUsername("HostUser");

        player2 = new User();
        player2.setId(2);
        player2.setUsername("Player2");

        game = new Game();
        game.setId(100);
        game.setHost(host);
        game.setRound(0);
        game.setGamePlayers(new ArrayList<>());
        
        gpHost = GamePlayer.build(game, host);
        gpHost.setId(10);
        
        gpPlayer2 = GamePlayer.build(game, player2);
        gpPlayer2.setId(11);
    }


    @Test
    void testFindAllGames() {
        when(gameRepository.findAll()).thenReturn(List.of(game));
        List<Game> games = gameService.findAll();
        assertFalse(games.isEmpty());
        assertEquals(1, games.size());
    }


    @Test
    void testFindGameById() {
        when(gameRepository.findById(100)).thenReturn(Optional.of(game));
        Game found = gameService.getGameById(100);
        assertEquals(100, found.getId());
    }


    @Test
    void testFindGameByIdNotFound() {
        when(gameRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.getGameById(999));
    }


    @Test
    void testGetGameByCode() {
        when(gameRepository.getGameByCode("CODE12")).thenReturn(Optional.of(game));
        Game found = gameService.getGameByCode("CODE12");
        assertEquals(game, found);
    }


    @Test
    void testGetGameByCodeNotFound() {
        when(gameRepository.getGameByCode("INVALID")).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.getGameByCode("INVALID"));
    }


    @Test
    void testCreateGame() {
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game created = gameService.createGame(host);

        assertNotNull(created);
        assertNotNull(created.getCode());
        assertEquals(6, created.getCode().length());
        assertEquals(host, created.getHost());
        assertEquals(1, created.getGamePlayers().size());
        assertEquals(0, created.getRound());
    }


    @Test
    void testJoinGameByCode() {
        game.getGamePlayers().add(gpHost);
        
        when(gameRepository.getGameByCode("CODE12")).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game joinedGame = gameService.joinGameByCode(player2, "CODE12");

        assertEquals(2, joinedGame.getGamePlayers().size());
        assertEquals(player2, joinedGame.getGamePlayers().get(1).getUser());
    }


    @Test
    void testStartGameSuccess() {
        game.getGamePlayers().add(gpHost);
        game.getGamePlayers().add(gpPlayer2);
        
        when(userService.findCurrentUser()).thenReturn(host);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        gameService.startGame(game);

        assertEquals(1, game.getRound());
        assertNotNull(game.getStartedAt());
        assertEquals(host.getId(), game.getTurn());
        verify(cardService, times(2)).initializeDeck(any(GamePlayer.class));
    }


    @Test
    void testStartGameNotHost() {
        when(userService.findCurrentUser()).thenReturn(player2);

        assertThrows(GameBadRequestException.class, () -> gameService.startGame(game), 
            "Only host can start the game");
    }


    @Test
    void testStartGameNotEnoughPlayers() {
        game.getGamePlayers().add(gpHost);
        when(userService.findCurrentUser()).thenReturn(host);

        assertThrows(GameBadRequestException.class, () -> gameService.startGame(game),
            "Game needs to have 2 players");
    }


    @Test
    void testStartGameAlreadyStarted() {
        game.setRound(1);
        game.getGamePlayers().add(gpHost);
        game.getGamePlayers().add(gpPlayer2);
        when(userService.findCurrentUser()).thenReturn(host);

        assertThrows(GameBadRequestException.class, () -> gameService.startGame(game),
            "Round needs to be 0");
    }


    @Test
    void testSetUpSkillSuccess() {
        game.setRound(2);
        gpHost.setEnergy(1);
        gpHost.setSkillsUsed(new ArrayList<>());

        Game result = gameService.setUpSkill(gpHost, Skill.SPEED_UP);

        assertEquals(0, gpHost.getEnergy());
        assertEquals(Skill.SPEED_UP, game.getSkill());
        assertEquals(1, gpHost.getSkillsUsed().size());
        verify(cardService).refillDeck(gpHost);
        verify(gameRepository).save(game);
    }


    @Test
    void testSetUpSkillLowEnergy() {
        game.setRound(2);
        gpHost.setEnergy(0);

        assertThrows(SkillBadRequestException.class, () -> gameService.setUpSkill(gpHost, Skill.SPEED_UP));
    }


    @Test
    void testSetUpSkillFirstRound() {
        game.setRound(1);
        gpHost.setEnergy(3);

        assertThrows(SkillBadRequestException.class, () -> gameService.setUpSkill(gpHost, Skill.SPEED_UP));
    }


    @Test
    void testAdvanceTurnNotFinished() {
        game.getGamePlayers().add(gpHost);
        game.getGamePlayers().add(gpPlayer2);
        
        when(gamePlayerService.getNextPlayer(game)).thenReturn(gpPlayer2);

        gameService.advanceToNextPlayer(game, game.getGamePlayers());

        assertEquals(player2.getId(), game.getTurn());
        assertNull(game.getSkill());
    }


    @Test
    void testStartNextRoundInitiative() {
        game.setRound(1);
        game.getGamePlayers().add(gpHost);
        game.getGamePlayers().add(gpPlayer2);

        when(gamePlayerCardService.getInitiatives(gpHost)).thenReturn(List.of(5));
        when(gamePlayerCardService.getInitiatives(gpPlayer2)).thenReturn(List.of(3));

        gameService.startNextRound(game, game.getGamePlayers());

        assertEquals(2, game.getRound());
        assertEquals(player2.getId(), game.getTurn());
        verify(cardService, times(2)).refillDeck(any(GamePlayer.class));
    }


    @Test
    void testStartNextRoundInitiativeTie() {
        game.setRound(1);
        game.getGamePlayers().add(gpHost);
        game.getGamePlayers().add(gpPlayer2);

        when(gamePlayerCardService.getInitiatives(gpHost)).thenReturn(List.of(5));
        when(gamePlayerCardService.getInitiatives(gpPlayer2)).thenReturn(List.of(5));

        gameService.startNextRound(game, game.getGamePlayers());

        assertEquals(host.getId(), game.getTurn());
    }


    @Test
    void testFinalizeGame() {
        game.setStartedAt(LocalDateTime.now().minusMinutes(10));
        game.getGamePlayers().add(gpHost);
        game.getGamePlayers().add(gpPlayer2);

        when(gameRepository.countGamesPlayedByUser(anyInt())).thenReturn(10L);
        when(gameRepository.countGameWinsByUser(anyInt())).thenReturn(5L);
        when(gameRepository.findFinishedGamesByUser(anyInt())).thenReturn(new ArrayList<>());

        Game finishedGame = gameService.finalizeGame(gpHost);

        assertNotNull(finishedGame.getEndedAt());
        assertEquals(host, finishedGame.getWinner());
        verify(achievementService, times(2)).unlockAchievements(any(), anyLong(), anyLong(), anyLong());
    }


    @Test
    void testGiveUpOrLose() {
        game.getGamePlayers().add(gpHost);
        game.getGamePlayers().add(gpPlayer2);

        when(gamePlayerService.getOpponent(gpHost)).thenReturn(gpPlayer2);

        when(gameRepository.countGamesPlayedByUser(anyInt())).thenReturn(0L);
        when(gameRepository.countGameWinsByUser(anyInt())).thenReturn(0L);
        when(gameRepository.findFinishedGamesByUser(anyInt())).thenReturn(new ArrayList<>());

        Game result = gameService.giveUpOrLose(gpHost);

        assertEquals(player2, result.getWinner());
    }


    @Test
    void testDeleteGameAsHost() {
        when(userService.findCurrentUser()).thenReturn(host);

        gameService.deleteGame(game);

        verify(gameRepository).deleteById(game.getId());
    }


    @Test
    void testDeleteGameAsNonHost() {
        when(userService.findCurrentUser()).thenReturn(player2);

        assertThrows(GameBadRequestException.class, () -> gameService.deleteGame(game));
        verify(gameRepository, never()).deleteById(any());
    }


    @Test
    void testDeleteUser() {
        User deletedUser = new User();
        deletedUser.setUsername("deleted");

        when(userService.findUser("deleted")).thenReturn(deletedUser);
        when(gameRepository.findByHost(host)).thenReturn(List.of(game));
        when(gameRepository.findByWinner(host)).thenReturn(new ArrayList<>());
        host.setGamePlayers(List.of(gpHost));

        gameService.deleteUser(host);

        assertEquals(deletedUser, game.getHost());
        assertEquals(deletedUser, gpHost.getUser());
        verify(userService).deleteUser(host);
    }

}