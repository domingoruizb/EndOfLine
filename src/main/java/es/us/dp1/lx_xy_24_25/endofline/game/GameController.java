package es.us.dp1.lx_xy_24_25.endofline.game;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Games", description = "The Games management API")
@SecurityRequirement(name = "bearerAuth")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(
        GameService gameService
    ) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> findAll() {
        return ResponseEntity.ok(gameService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Integer id) {
        Game game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

    // TODO: Use authenticated user as host
    @PostMapping("/create/{hostId}")
    public ResponseEntity<Game> createGame(@PathVariable Integer hostId) {
        Game game = gameService.createGame(hostId);
        return ResponseEntity.ok(game);
    }

    // TODO: Use authenticated user as joining user
    @PostMapping("/join/{userId}/{code}")
    public ResponseEntity<Game> joinGame(@PathVariable Integer userId, @PathVariable String code) {
        Game game = gameService.joinGameByCode(userId, code);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Game> startGame(@PathVariable Integer id) {
        Game game = gameService.startGame(id);
        return ResponseEntity.ok(game);
    }

    // TODO: Use authenticated user as giving up user
    @PutMapping("/{gameId}/{userId}/giveup")
    public ResponseEntity<Game> giveUp(@PathVariable Integer gameId, @PathVariable Integer userId) {
        // Handle spectating users trying to give up
        Game game = gameService.getGameById(gameId);
        List<Integer> user = game.getGamePlayers().stream().map(p -> p.getUser().getId()).toList();

        if (!user.contains(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Game updatedGame = gameService.giveUpOrLose(gameId, userId);
        return ResponseEntity.ok(updatedGame);
    }

    // TODO: Use authenticated user as setting up skill user
    @PutMapping("/{gameId}/{userId}/setUpSkill")
    public ResponseEntity<Game> setUpSkill(@PathVariable Integer gameId, @PathVariable Integer userId, @RequestBody SkillRequestDTO dto) {
        Game game = gameService.setUpSkill(gameId, userId, dto.getSkill());
        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Integer id) {
        Game game = gameService.getGameById(id);
        gameService.deleteGame(game);
    }
}
