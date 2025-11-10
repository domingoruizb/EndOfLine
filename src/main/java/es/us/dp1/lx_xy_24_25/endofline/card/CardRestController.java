package es.us.dp1.lx_xy_24_25.endofline.card;

import java.util.List;

import es.us.dp1.lx_xy_24_25.endofline.auth.payload.response.MessageResponse;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.BadRequestException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
		if (card == null)
			throw new ResourceNotFoundException("Card with id " + id + " not found!");
		return new ResponseEntity<>(card, HttpStatus.OK);
	}

	@GetMapping("/gameplayer/{gpId}")
	public ResponseEntity<List<Card>> findByGamePlayer(@PathVariable("gpId") Integer gpId) {
		return new ResponseEntity<>(cardService.findByGamePlayerId(gpId), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Card> createCard(@RequestBody @Valid Card newCard, BindingResult br) {
		if (br.hasErrors())
			throw new BadRequestException(br.getAllErrors());
		Card result = cardService.saveCard(newCard);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Card> modifyCard(@RequestBody @Valid Card newCard, BindingResult br,
										   @PathVariable("id") Integer id) {
		Card cardToUpdate = this.findCard(id).getBody();
		if (br.hasErrors())
			throw new BadRequestException(br.getAllErrors());
		else if (newCard.getId() == null || !newCard.getId().equals(id))
			throw new BadRequestException("Card id is not consistent with resource URL:" + id);
		else {
			BeanUtils.copyProperties(newCard, cardToUpdate, "id", "gamePlayer");
			cardService.saveCard(cardToUpdate);
		}
		return new ResponseEntity<>(cardToUpdate, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResponse> deleteCard(@PathVariable("id") Integer id) {
		findCard(id);
		cardService.deleteById(id);
		return new ResponseEntity<>(new MessageResponse("Card deleted!"), HttpStatus.OK);
	}
}
