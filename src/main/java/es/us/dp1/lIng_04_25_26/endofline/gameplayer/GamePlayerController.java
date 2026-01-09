package es.us.dp1.lIng_04_25_26.endofline.gameplayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gameplayers")
public class GamePlayerController {
    private GamePlayerService gamePlayerService;

    @Autowired
    public GamePlayerController(GamePlayerService gamePlayerService) {
        this.gamePlayerService = gamePlayerService;
    }

    @PutMapping("/{gameId}/{userId}/color")
    public ResponseEntity<GamePlayerDTO> updatePlayerColor(@PathVariable Integer gameId, @PathVariable Integer userId, @RequestBody String unformattedColor) {
        GamePlayer updatedGamePlayer = gamePlayerService.updatePlayerColor(gameId, userId, unformattedColor);
        return ResponseEntity.ok(new GamePlayerDTO(updatedGamePlayer));
    }

}
