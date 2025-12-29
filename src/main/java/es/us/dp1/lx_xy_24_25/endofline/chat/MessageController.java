package es.us.dp1.lx_xy_24_25.endofline.chat;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerRepository;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games/{gameId}/chat")
public class MessageController {

    private final UserRepository userRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(
        UserRepository userRepository,
        GamePlayerRepository gamePlayerRepository,
        MessageRepository messageRepository
    ) {
        this.userRepository = userRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.messageRepository = messageRepository;
    }

    public static class ChatRequest {
        public String text;
        public ChatRequest() {}
    }

    @PostMapping
    public ResponseEntity<MessageDTO> postMessage(@PathVariable Long gameId, @RequestBody ChatRequest req, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        if (req == null || req.text == null || req.text.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userRepository.findByUsername(principal.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        User user = userOpt.get();

        // Allow both players and spectators to send messages
        Optional<GamePlayer> gpOpt = gamePlayerRepository.findByGameIdAndUserId(gameId.intValue(), user.getId());
        
        Message msg = new Message();
        msg.setBody(req.text.trim());
        msg.setCreatedAt(Instant.now());
        msg.setGameId(gameId.intValue());
        msg.setSenderUsername(user.getUsername());
        
        if (gpOpt.isPresent()) {
            msg.setGamePlayer(gpOpt.get());
        }

        Message saved = messageRepository.save(msg);

        MessageDTO dto = new MessageDTO(user.getUsername(), saved.getBody());
        dto.setTimestamp(saved.getCreatedAt().toEpochMilli());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long gameId, @RequestParam(name = "since", required = false, defaultValue = "0") long since) {
        List<Message> messages;
        if (since > 0) {
            messages = messageRepository.findByGameIdAndCreatedAtAfterOrderByCreatedAtAsc(gameId.intValue(), Instant.ofEpochMilli(since));
        } else {
            messages = messageRepository.findByGameIdOrderByCreatedAtAsc(gameId.intValue());
        }

        List<MessageDTO> dtos = messages.stream().map(m -> {
            String sender;
            if (m.getSenderUsername() != null) {
                sender = m.getSenderUsername();
            } else if (m.getGamePlayer() != null && m.getGamePlayer().getUser() != null) {
                sender = m.getGamePlayer().getUser().getUsername();
            } else {
                sender = "anonymous";
            }
            MessageDTO cm = new MessageDTO(sender, m.getBody());
            cm.setTimestamp(m.getCreatedAt().toEpochMilli());
            return cm;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // optional helper to clear messages for a game (not exposed publicly)
    public void clearGameMessages(Long gameId) {
        // remove messages for game by deleting message rows for players in the game
        List<Message> list = messageRepository.findByGamePlayer_Game_IdOrderByCreatedAtAsc(gameId.intValue());
        messageRepository.deleteAll(list);
    }
}
