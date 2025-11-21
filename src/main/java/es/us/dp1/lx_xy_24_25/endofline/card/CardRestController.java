package es.us.dp1.lx_xy_24_25.endofline.card;

import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@Tag(name = "Cards", description = "The Cards management API")
@SecurityRequirement(name = "bearerAuth")
public class CardRestController {

	private final CardService cardService;
    private final GamePlayerCardService gamePlayerCardService;
    private final GamePlayerService gamePlayerService;

	@Autowired
	public CardRestController(
        CardService cardService,
        GamePlayerCardService gamePlayerCardService,
        GamePlayerService gamePlayerService
    ) {
		this.cardService = cardService;
        this.gamePlayerCardService = gamePlayerCardService;
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

	@GetMapping("/color/{color}")
	public ResponseEntity<List<Card>> findByColor(@PathVariable("color") String color) {
		return new ResponseEntity<>(cardService.getCardsByColor(color), HttpStatus.OK);
	}

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<GamePlayerCard>> findByGameId(@PathVariable("gameId") Integer gameId) {
        return new ResponseEntity<>(cardService.findByGameId(gameId), HttpStatus.OK);
    }

    @PostMapping("/place/{gamePlayerId}")
    public ResponseEntity<GamePlayerCard> placeCard(
        @PathVariable("gamePlayerId") Integer gamePlayerId,
        @RequestBody GamePlayerCardDTO gamePlayerCardDTO
    ) {
        GamePlayer gamePlayer = gamePlayerService.getById(gamePlayerId);

        if (!gamePlayerService.isValidTurn(gamePlayer)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Card card = cardService.findByImage(gamePlayerCardDTO.getImage());

        GamePlayerCard gamePlayerCard = GamePlayerCard.buildGamePlayerCard(
            gamePlayerCardDTO,
            gamePlayer,
            card
        );

        GamePlayerCard saved = gamePlayerCardService.placeCard(gamePlayerCard, gamePlayerCardDTO.getTurnFinished());

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
