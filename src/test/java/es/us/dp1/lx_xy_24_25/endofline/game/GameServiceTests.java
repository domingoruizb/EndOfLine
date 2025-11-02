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

import jakarta.transaction.Transactional;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;

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
}
