package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gpc")
@CrossOrigin(origins = "*")
public class GamePlayerCardController {

    private final GamePlayerCardService gpcService;

    @Autowired
    public GamePlayerCardController(GamePlayerCardService gpcService) {
        this.gpcService = gpcService;
    }
}
