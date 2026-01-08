package es.us.dp1.lx_xy_24_25.endofline.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.game.GameService;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class GameServiceTests {

    @Autowired
    private GameService gameService;

    @Test
    public void findAllGamesTest() {
        Iterable<Game> allGames = this.gameService.findAll();
        long count = StreamSupport.stream(allGames.spliterator(), false).count();
        assertNotEquals(0, count);
    }

    @Test
    void findGameByIdTest() {
        Game game = gameService.getGameById(1);
        assertEquals(1, game.getRound());
        assertEquals(1, game.getId());
    }

    @Test
    void cannotFindGameByIdTest() {
        assertThrows(ResourceNotFoundException.class, () -> gameService.getGameById(100000));
    }

    @Test
    @Transactional
    public void createGameTest() {
        Game game = gameService.createGame(1);
        assertNotEquals(null, game);
        assertNotEquals(null, game.getStartedAt());
        assertEquals(0, game.getRound());
        assertEquals(null, game.getEndedAt());
    }

    @Test
    void startGameTest() {
        Game game = gameService.startGame(1);
        assertNotEquals(null, game.getStartedAt());
        assertEquals(1, game.getRound());
    }

    @Test
    @Transactional
    void joinGameByCodeTest() {
        Game created = gameService.createGame(1);
        String code = created.getCode();
        Game joined = gameService.joinGameByCode(4, code);
        assertEquals(created.getId(), joined.getId());
        assertTrue(
            joined.getGamePlayers()
                .stream()
                .anyMatch(gp -> gp.getUser().getId().equals(4))
        );
    }

    @Test
    void joinGameByCodeInvalidCodeTest() {
        assertThrows(ResourceNotFoundException.class, () -> gameService.joinGameByCode(4, "XXXXXX"));
    }

    @Test
    @Transactional
    void advanceTurnTest() {
        Game game = gameService.createGame(1);
        assertEquals(0, game.getRound(), "El round inicial debe ser 0");
        gameService.advanceTurn(game);
        Game updated = gameService.getGameById(game.getId());
        assertEquals(0, updated.getRound(),
            "advanceTurn no debe avanzar la ronda si no hay 2 jugadores");
    }

    @Test
    void advanceTurnNullGameTest() {
        assertThrows(NullPointerException.class, () -> gameService.advanceTurn(null));
    }

    @Test
    @Transactional
    void endGameTest() {
        Game game = gameService.endGame(1, 4);
        assertNotNull(game.getEndedAt());
        assertNotNull(game.getWinner());
        assertEquals(4, game.getWinner().getId());
    }

    @Test
    void endGameInvalidGameIdTest() {
        assertThrows(ResourceNotFoundException.class, () -> gameService.endGame(9999, 1));
    }

    @Test
    @Transactional
    void giveUpOrLoseDeclaresOpponentWinnerTest() {
        Game g = gameService.createGame(1);
        g = gameService.joinGameByCode(4, g.getCode());
        Game after = gameService.giveUpOrLose(g.getId(), 1);
        assertNotNull(after.getWinner(), "After giveUpOrLose winner must be assigned");
        assertEquals(4, after.getWinner().getId().intValue());
    }

    @Test
    void giveUpOrLoseInvalidGameOrPlayerTest() {
        assertThrows(ResourceNotFoundException.class, () -> gameService.giveUpOrLose(99999, 1));
    }

    @Test
    @Transactional
    void setUpSkillReducesEnergyAndSetsSkillTest() {
        Game g = gameService.createGame(1);
        g = gameService.joinGameByCode(4, g.getCode());
        Game result = gameService.setUpSkill(g.getId(), 1, "SPEED_UP");
        assertNotNull(result, "setUpSkill should return the game instance");
        assertEquals(Skill.SPEED_UP, result.getSkill(), "Game.skill must be set to SPEED_UP");
        GamePlayer hostGp = result.getGamePlayers().stream()
            .filter(gp -> gp.getUser() != null && gp.getUser().getId().equals(1))
            .findFirst()
            .orElse(null);
        assertNotNull(hostGp, "Host GamePlayer must exist");
        assertTrue(hostGp.getEnergy() <= 2, "Host energy must have been reduced by setUpSkill (initial was 3)");
    }

    @Test
    void setUpSkillInvalidGameOrUserTest() {
        assertThrows(ResourceNotFoundException.class, () -> gameService.setUpSkill(999999, 1, "SPEED_UP"));
        Game created = gameService.createGame(1);
        assertThrows(RuntimeException.class, () -> gameService.setUpSkill(created.getId(), 999999, "SPEED_UP"));
    }

    @Test
    @Transactional
    void deleteGameTest() {
        Game g = gameService.createGame(1);
        Integer id = g.getId();
        gameService.deleteGame(id);
        assertThrows(ResourceNotFoundException.class, () -> gameService.getGameById(id));
    }

    @Test
    @Transactional
    void gamePlayersConsistencyTest() {
        Game g = gameService.getGameById(1);
        assertNotNull(g.getGamePlayers());
        assertTrue(g.getGamePlayers().size() > 0);
        for (GamePlayer gp : g.getGamePlayers()) {
            assertEquals(g.getId(), gp.getGame().getId());
        }
    }

}
