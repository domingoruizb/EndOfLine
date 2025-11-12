package es.us.dp1.lx_xy_24_25.endofline.card;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardDTO;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@Tag(name = "Cards", description = "The Cards management API")
@SecurityRequirement(name = "bearerAuth")
public class CardRestController {

	private final CardService cardService;
    private final GamePlayerCardService gpcService;
    private final GamePlayerService gamePlayerService;

	@Autowired
	public CardRestController(
        CardService cardService,
        GamePlayerCardService gpcService,
        GamePlayerService gamePlayerService
    ) {
		this.cardService = cardService;
        this.gpcService = gpcService;
        this.gamePlayerService = gamePlayerService;
	}

	@GetMapping
	public ResponseEntity<List<Card>> findAll() {
		return new ResponseEntity<>(cardService.findAll(), HttpStatus.OK);
	}

    @GetMapping("/image/{image}")
    public ResponseEntity<Card> findByImage(@PathVariable("image") String image) {
        return new ResponseEntity<>(cardService.findByImage(image), HttpStatus.OK);
    }

	@GetMapping("/lineColor/{color}")
	public ResponseEntity<List<Card>> findByColor(@PathVariable("color") String color) {
		return new ResponseEntity<>(cardService.getCardsByColor(color), HttpStatus.OK);
	}

    @PostMapping("/place/{gamePlayerId}")
    public ResponseEntity<GamePlayerCard> placeCard(
        @PathVariable("gamePlayerId") Integer gamePlayerId,
        @RequestBody GamePlayerCardDTO dto
    ) {
        GamePlayerCard gpc = new GamePlayerCard();
        Card card = cardService.findByImage(dto.getImage());
        GamePlayer gp = gamePlayerService.getById(gamePlayerId);

        gpc.setCard(card);
        gpc.setGamePlayer(gp);
        gpc.setPositionX(dto.getPositionX());
        gpc.setPositionY(dto.getPositionY());
        gpc.setRotation(dto.getRotation());
        gpc.setPlacedAt(LocalDateTime.now());

        return new ResponseEntity<>(gpcService.placeCard(gpc), HttpStatus.CREATED);
    }
}
