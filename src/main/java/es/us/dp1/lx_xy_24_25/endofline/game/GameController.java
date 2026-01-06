package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
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
    private final UserService userService;
    private final GamePlayerService gamePlayerService;

    @Autowired
    public GameController(
        GameService gameService,
        UserService userService,
        GamePlayerService gamePlayerService) {
        this.gameService = gameService;
        this.userService = userService;
        this.gamePlayerService = gamePlayerService;
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

    // TODO: Check that only host can start the game
    @PostMapping("/{id}/start")
    public ResponseEntity<Game> startGame(@PathVariable Integer id) {
        Game game = gameService.startGame(id);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/{gameId}/giveup")
    public ResponseEntity<Game> giveUp(@PathVariable Integer gameId) {
        User user = userService.findCurrentUser();
        GamePlayer gamePlayer = gamePlayerService.getGamePlayer(gameId, user.getId());
        Game updatedGame = gameService.giveUpOrLose(gamePlayer);
        return ResponseEntity.ok(updatedGame);
    }

    @PutMapping("/{gameId}/setUpSkill")
    public ResponseEntity<Game> setUpSkill(@PathVariable Integer gameId, @RequestBody SkillRequestDTO dto) {
        User user = userService.findCurrentUser();
        GamePlayer gamePlayer = gamePlayerService.getGamePlayer(gameId, user.getId());
        Game game = gameService.setUpSkill(gamePlayer, dto.toSkillEnum());
        return ResponseEntity.ok(game);
    }

    // TODO: Check that only host can finalize the game
    @DeleteMapping("/{gameId}")
    public void deleteGame(@PathVariable Integer gameId) {
        Game game = gameService.getGameById(gameId);
        gameService.deleteGame(game);
    }
}
