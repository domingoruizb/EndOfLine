package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GamePlayer> updatePlayerColor(@PathVariable Integer gameId, @PathVariable Integer userId, @RequestBody String newColor) {
        try {
            String cleanedColor = newColor.replace("\"", "").trim();

            GamePlayer updatedGamePlayer = gamePlayerService.updatePlayerColor(gameId, userId, cleanedColor);

            return new ResponseEntity<>(updatedGamePlayer, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{gameId}/{userId}")
    public ResponseEntity<GamePlayer> getGamePlayer(@PathVariable Integer gameId, @PathVariable Integer userId) {
        try {
            GamePlayer gamePlayer = gamePlayerService.getGamePlayer(gameId, userId);
            return new ResponseEntity<>(gamePlayer, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
