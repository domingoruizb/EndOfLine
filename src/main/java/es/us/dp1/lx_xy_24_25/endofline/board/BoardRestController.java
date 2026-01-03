package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.board.dto.BoardCardDTO;
import es.us.dp1.lx_xy_24_25.endofline.board.dto.BoardPlayerDTO;
import es.us.dp1.lx_xy_24_25.endofline.board.dto.BoardStateDTO;
import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.card.CardService;
import es.us.dp1.lx_xy_24_25.endofline.configuration.DecodeJWT;
import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardDTO;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@Tag(name = "Cards", description = "The board management API")
@SecurityRequirement(name = "bearerAuth")
public class BoardRestController {

    private final CardService cardService;
    private final GamePlayerService gamePlayerService;
    private final BoardService boardService;
    private final GamePlayerCardService gamePlayerCardService;

    @Autowired
    public BoardRestController(
        CardService cardService,
        GamePlayerService gamePlayerService,
        BoardService boardService,
        GamePlayerCardService gamePlayerCardService
    ) {
        this.cardService = cardService;
        this.gamePlayerService = gamePlayerService;
        this.boardService = boardService;
        this.gamePlayerCardService = gamePlayerCardService;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<BoardStateDTO> getState (
        @PathVariable Integer gameId
    ) {
        User user = DecodeJWT.getUserFromJWT();
        GamePlayer gamePlayer = gamePlayerService.getGamePlayer(gameId, user.getId());

        if (gamePlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GamePlayerCard lastPlacedCard = gamePlayerCardService.getLastPlacedCard(gamePlayer);

        List<GamePlayerCard> board = boardService.getBoard(gamePlayer.getGame().getId());

        Boolean isTurn = gamePlayerService.isValidTurn(gamePlayer);
        Boolean isHost = gamePlayerService.isHost(gamePlayer);
        Integer energy = gamePlayer.getEnergy();
        Integer turn = gamePlayer.getGame().getTurn();
        Integer round = gamePlayer.getGame().getRound();

        // Don't return placeable positions if it's not the player's turn
        // If it's the first turn, return initial valid positions
        // Otherwise, return valid positions based on the last placed card
        List<Integer> placeablePositions = isTurn ? (lastPlacedCard != null ? BoardUtils.getValidIndexes(
            lastPlacedCard,
            board
        ) : BoardUtils.getInitialValidIndexes(isHost)) : List.of();

        // Only return reversible positions if it's the player's turn
        List<Integer> reversiblePositions = isTurn && lastPlacedCard != null ? boardService.getReversiblePositions(gamePlayer) : List.of();

        List<BoardPlayerDTO> players = gamePlayer.getGame().getGamePlayers().stream().map(p -> new BoardPlayerDTO(
            p.getId(),
            p.getUser().getId(),
            p.getColor().toString(),
            p.getUser().getUsername(),
            gamePlayerService.isHost(p)
        )).toList();

        LocalDateTime startedAt = gamePlayer.getGame().getStartedAt();

        List<BoardCardDTO> cards = board.stream().map(c -> new BoardCardDTO(
            c.getGamePlayer().getId(),
            BoardUtils.getIndex(c.getPositionX(), c.getPositionY()),
            c.getRotation(),
            c.getPlacedAt(),
            c.getCard()
        )).toList();

        Skill skill = isTurn ? gamePlayer.getGame().getSkill() : null;

        BoardStateDTO boardStateDTO = new BoardStateDTO(
            user.getId(),
            gamePlayer.getGame().getId(),
            gamePlayer.getId(),
            startedAt,
            placeablePositions,
            reversiblePositions,
            energy,
            turn,
            round,
            players,
            cards,
            skill
        );

        return new ResponseEntity<>(boardStateDTO, HttpStatus.OK);
    }

    // TODO: Not used in frontend
    @PostMapping("/{gameId}")
    public ResponseEntity<GamePlayerCard> placeCard (
        @PathVariable Integer gameId,
        @RequestBody GamePlayerCardDTO gamePlayerCardDTO
    ) {
        User user = DecodeJWT.getUserFromJWT();
        GamePlayer gamePlayer = gamePlayerService.getGamePlayer(gameId, user.getId());

        if (gamePlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!gamePlayerService.isValidTurn(gamePlayer)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Card card = cardService.findByImage(gamePlayerCardDTO.getImage());

        GamePlayerCard gamePlayerCard = GamePlayerCard.buildGamePlayerCard(
            gamePlayerCardDTO,
            gamePlayer,
            card
        );

        GamePlayerCard saved = boardService.placeCard(gamePlayerCard);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}
