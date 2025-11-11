package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gameplayercards")
@CrossOrigin(origins = "*")
public class GamePlayerCardController {

    private final GamePlayerCardService service;

    public GamePlayerCardController(GamePlayerCardService service) {
        this.service = service;
    }

    //Get all visible cards for a player within a game.
    // Returns: all on-board cards + the viewer's own cards.
    @GetMapping("/game/{gameId}/viewer/{viewerGamePlayerId}")
    public ResponseEntity<List<GamePlayerCardDTO>> getVisibleCards(
            @PathVariable("gameId") Integer gameId,
            @PathVariable("viewerGamePlayerId") Integer viewerGamePlayerId) {

        List<GamePlayerCardDTO> result = service.getVisibleCards(gameId, viewerGamePlayerId);
        return ResponseEntity.ok(result);
    }

    // Get all on-board cards in a game (public info).
    @GetMapping("/game/{gameId}/onboard")
    public ResponseEntity<List<GamePlayerCardDTO>> getOnBoardCards(@PathVariable("gameId") Integer gameId) {
        List<GamePlayerCardDTO> result = service.getOnBoardCards(gameId);
        return ResponseEntity.ok(result);
    }
}
