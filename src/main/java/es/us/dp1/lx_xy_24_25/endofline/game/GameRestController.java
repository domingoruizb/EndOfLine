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
public class GameRestController {

    private final GameService gameService;

    @Autowired
    public GameRestController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> findAll() {
        return new ResponseEntity<>((List<Game>) this.gameService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Integer id) {
        Game game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/create/{hostId}")
    public ResponseEntity<Game> createGame(@PathVariable Integer hostId) {
        Game game = gameService.createGame(hostId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Game> startGame(@PathVariable Integer id) {
        Game game = gameService.startGame(id);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/join/{userId}/{code}")
    public ResponseEntity<Game> startGame(@PathVariable Integer userId, @PathVariable String code) {
        Game game = gameService.joinGameByCode(userId, code);
        return ResponseEntity.ok(game);
    }

//    @PostMapping("/{id}/end/{winnerId}")
//    public ResponseEntity<Game> endGame(@PathVariable Integer id, @PathVariable Integer winnerId) {
//        Game game = gameService.endGame(id, winnerId);
//        return ResponseEntity.ok(game);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Integer id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
