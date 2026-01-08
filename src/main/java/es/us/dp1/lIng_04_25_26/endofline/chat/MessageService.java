package es.us.dp1.lIng_04_25_26.endofline.chat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.chat.dto.MessageRequestDTO;
import es.us.dp1.lIng_04_25_26.endofline.chat.dto.MessageResponseDTO;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

import java.time.Instant;
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
        Message msg = Message.build(request.trimmedText(), Instant.now().toEpochMilli(), user, game);
        Message saved = messageRepository.save(msg);
        return MessageResponseDTO.build(saved);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessagesBySince (Integer gameId, Long since) {
        List<Message> messages = messageRepository.findMessages(gameId, since);

        return messages
            .stream()
            .map(MessageResponseDTO::build)
            .toList();
    }

}
