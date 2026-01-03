package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.board.BoardService;
import es.us.dp1.lx_xy_24_25.endofline.board.BoardUtils;
import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.card.CardService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardDTO;
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
    private final BoardService boardService;
    private final GamePlayerService gamePlayerService;
    private final CardService cardService;

    @Autowired
    public GameRestController(
        GameService gameService,
        BoardService boardService,
        GamePlayerService gamePlayerService,
        CardService cardService
    ) {
        this.gameService = gameService;
        this.boardService = boardService;
        this.gamePlayerService = gamePlayerService;
        this.cardService = cardService;
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

    @PostMapping("/{id}/next-turn")
    public ResponseEntity<Game> nextTurn(@PathVariable Integer id) {
        Game game = gameService.getGameById(id);
        gameService.advanceTurn(game);
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @PostMapping("/{id}/end/{winnerId}")
    public ResponseEntity<Game> endGame(@PathVariable Integer id, @PathVariable Integer winnerId) {
        return ResponseEntity.ok(gameService.endGame(id, winnerId));
    }

    @PutMapping("/{gameId}/{userId}/giveup")
    public ResponseEntity<Game> giveUp(@PathVariable Integer gameId, @PathVariable Integer userId) {
        Game game = gameService.giveUpOrLose(gameId, userId);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/{gameId}/{userId}/lose")
    public ResponseEntity<Game> lose(@PathVariable Integer gameId, @PathVariable Integer userId) {
        Game game = gameService.giveUpOrLose(gameId, userId);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/{gameId}/{userId}/setUpSkill")
    public ResponseEntity<Game> setUpSkill(@PathVariable Integer gameId, @PathVariable Integer userId, @RequestBody SkillRequestDTO dto) {
        Game game = gameService.setUpSkill(gameId, userId, dto.getSkill());
        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Integer id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
