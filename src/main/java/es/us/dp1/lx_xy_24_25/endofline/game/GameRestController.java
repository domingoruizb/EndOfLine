package es.us.dp1.lx_xy_24_25.endofline.game;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
