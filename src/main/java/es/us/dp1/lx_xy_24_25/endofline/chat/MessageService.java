package es.us.dp1.lx_xy_24_25.endofline.chat;

import es.us.dp1.lx_xy_24_25.endofline.chat.dto.MessageRequestDTO;
import es.us.dp1.lx_xy_24_25.endofline.chat.dto.MessageResponseDTO;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(
        MessageRepository messageRepository
    ) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public MessageResponseDTO saveMessage (MessageRequestDTO request, User user, Game game) {
        Message msg = new Message();

        msg.setBody(request.trimmedText());
        msg.setSentAt(LocalDateTime.now());
        msg.setGame(game);
        msg.setUser(user);

        Message saved = messageRepository.save(msg);

        return MessageResponseDTO.build(saved);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessages (Integer gameId, LocalDateTime since) {
        List<Message> messages = messageRepository.findMessages(gameId, since);

        return messages
            .stream()
            .map(MessageResponseDTO::build).toList();
    }

}
