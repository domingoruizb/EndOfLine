package es.us.dp1.lx_xy_24_25.endofline.chat;

import es.us.dp1.lx_xy_24_25.endofline.chat.dto.MessageRequestDTO;
import es.us.dp1.lx_xy_24_25.endofline.chat.dto.MessageResponseDTO;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final GameService gameService;
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(
        MessageRepository messageRepository,
        GameService gameService,
        MessageService messageService,
        UserService userService
    ) {
        this.messageRepository = messageRepository;
        this.gameService = gameService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/{gameId}")
    public ResponseEntity<MessageResponseDTO> postMessage(
        @PathVariable Integer gameId,
        @RequestBody @Valid MessageRequestDTO request
    ) {
        User user = userService.findCurrentUser();
        Game game = gameService.getGameById(gameId);

        MessageResponseDTO response = messageService.saveMessage(request, user, game);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessages(
        @PathVariable Integer gameId,
        @RequestParam(required = false) String since
    ) {
        Long sinceValue = (since == null) ? null : Long.valueOf(since);
        List<MessageResponseDTO> response = messageService.getMessages(gameId, sinceValue);

        return ResponseEntity.ok(response);
    }

}
