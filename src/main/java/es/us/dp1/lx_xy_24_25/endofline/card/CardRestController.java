package es.us.dp1.lx_xy_24_25.endofline.card;

import java.util.List;

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

	@Autowired
	public CardRestController(CardService cardService) {
		this.cardService = cardService;
	}

	@GetMapping
	public ResponseEntity<List<Card>> findAll() {
		return new ResponseEntity<>((List<Card>) cardService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Card> findCard(@PathVariable("id") Integer id) {
		Card card = cardService.getById(id);
		return new ResponseEntity<>(card, HttpStatus.OK);
	}

	@GetMapping("/color/{color}")
	public ResponseEntity<List<Card>> findByColor(@PathVariable("color") String color) {
		return new ResponseEntity<>(cardService.getCardsByColor(color), HttpStatus.OK);
	}
	/*

	@GetMapping("/gameplayer/{gpId}")
	public ResponseEntity<List<Card>> findByGamePlayer(@PathVariable("gpId") Integer gpId) {
		return new ResponseEntity<>(cardService.getCardsByGamePlayer(gpId), HttpStatus.OK);
	}

	@GetMapping("/games/{gameId}/onboard")
	public ResponseEntity<List<Card>> getOnBoardByGame(@PathVariable("gameId") Integer gameId) {
		return new ResponseEntity<>(cardService.getOnBoardCards(gameId), HttpStatus.OK);
	}

	@GetMapping("/games/{gameId}/visible/{viewerGpId}")
	public ResponseEntity<List<Card>> getVisible(@PathVariable("gameId") Integer gameId,
												 @PathVariable("viewerGpId") Integer viewerGpId) {
		return new ResponseEntity<>(cardService.getVisibleCards(gameId, viewerGpId), HttpStatus.OK);
	}*/
}