package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.board.dto.BoardPlaceDTO;
import es.us.dp1.lx_xy_24_25.endofline.board.dto.BoardStateDTO;
import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.card.CardService;
import es.us.dp1.lx_xy_24_25.endofline.configuration.DecodeJWT;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@Tag(name = "Cards", description = "The board management API")
@SecurityRequirement(name = "bearerAuth")
public class BoardController {

    private final CardService cardService;
    private final GamePlayerService gamePlayerService;
    private final BoardService boardService;
    private final GameService gameService;

    @Autowired
    public BoardController(
        CardService cardService,
        GamePlayerService gamePlayerService,
        BoardService boardService,
        GameService gameService
    ) {
        this.cardService = cardService;
        this.gamePlayerService = gamePlayerService;
        this.boardService = boardService;
        this.gameService = gameService;
    }

    @GetMapping("/{gameId}/state")
    public ResponseEntity<BoardStateDTO> getState (
        @PathVariable Integer gameId
    ) {
        User user = DecodeJWT.getUserFromJWT();
        Game game = gameService.getGameById(gameId);
        Boolean isSpectating = gamePlayerService.isSpectating(game, user);
        GamePlayer gamePlayer = gamePlayerService.getGamePlayerOrFriend(game, user);

        BoardStateDTO state = boardService.getState(gamePlayer, game, isSpectating);

        return ResponseEntity.ok(state);
    }

    @PostMapping("/{gameId}/deck")
    public ResponseEntity<List<Card>> getDeckCards (
        @PathVariable Integer gameId,
        @RequestBody List<Card> deck
    ) {
        User user = DecodeJWT.getUserFromJWT();
        Game game = gameService.getGameById(gameId);
        Boolean isSpectating = gamePlayerService.isSpectating(game, user);

        GamePlayer gamePlayer = isSpectating ? null : gamePlayerService.getGamePlayer(gameId, user.getId());
        List<Card> updatedDeck = isSpectating ? List.of() : boardService.getDeckCards(gamePlayer, deck);

        return ResponseEntity.ok(updatedDeck);
    }

    @PostMapping("/{gameId}/place")
    public ResponseEntity<Void> placeCard (
        @PathVariable Integer gameId,
        @RequestBody @Valid BoardPlaceDTO boardPlaceDTO
    ) {
        User user = DecodeJWT.getUserFromJWT();
        GamePlayer gamePlayer = gamePlayerService.getGamePlayer(gameId, user.getId());

        Card card = cardService.findById(boardPlaceDTO.getCardId());

        boardService.placeCard(gamePlayer, card, boardPlaceDTO.getIndex());

        return ResponseEntity.noContent().build();
    }

}
