package es.us.dp1.lx_xy_24_25.endofline.chat;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
public class ChatController {

    // in-memory storage of messages per game
    private final Map<Long, CopyOnWriteArrayList<ChatMessage>> messages = new ConcurrentHashMap<>();

    public static class ChatRequest {
        public String text;
        public ChatRequest() {}
    }

    @PostMapping
    public ResponseEntity<ChatMessage> postMessage(@PathVariable Long gameId, @RequestBody ChatRequest req, Principal principal) {
        String sender = principal != null ? principal.getName() : "anonymous";
        if (req == null || req.text == null || req.text.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ChatMessage msg = new ChatMessage(sender, req.text.trim());
        messages.computeIfAbsent(gameId, k -> new CopyOnWriteArrayList<>()).add(msg);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long gameId, @RequestParam(name = "since", required = false, defaultValue = "0") long since) {
        List<ChatMessage> list = messages.getOrDefault(gameId, new CopyOnWriteArrayList<>());
        List<ChatMessage> filtered = list.stream().filter(m -> m.getTimestamp() > since).collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    // optional helper to clear messages for a game (not exposed publicly)
    public void clearGameMessages(Long gameId) {
        messages.remove(gameId);
    }
}
