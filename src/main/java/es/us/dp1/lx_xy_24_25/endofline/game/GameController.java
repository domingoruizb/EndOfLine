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

    @PostMapping("/create")
    public ResponseEntity<Game> createGame() {
        User user = userService.findCurrentUser();
        Game game = gameService.createGame(user.getId());
        return ResponseEntity.ok(game);
    }

    @PostMapping("/join/{code}")
    public ResponseEntity<Game> joinGame(@PathVariable String code) {
        User user = userService.findCurrentUser();
        Game game = gameService.joinGameByCode(user.getId(), code);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Game> startGame(@PathVariable Integer id) {
        User user = userService.findCurrentUser();
        Game game = gameService.getGameById(id);
        if (!game.getHost().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Game startedGame = gameService.startGame(id);
        return ResponseEntity.ok(startedGame);
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

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Integer gameId) {
        User user = userService.findCurrentUser();
        Game game = gameService.getGameById(gameId);
        if (!game.getHost().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        gameService.deleteGame(game);
        return ResponseEntity.noContent().build();
    }
}
