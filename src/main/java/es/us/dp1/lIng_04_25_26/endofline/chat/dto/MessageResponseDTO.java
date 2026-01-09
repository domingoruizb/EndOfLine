package es.us.dp1.lIng_04_25_26.endofline.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import es.us.dp1.lIng_04_25_26.endofline.chat.Message;

// DTO for returning messages to player
// 'body' for message text, 'timestamp' as epoch milliseconds
@Getter
@Setter
public class MessageResponseDTO {

    private Integer id;
    private String sender;
    private String body;
    private Long sentAt;

    public MessageResponseDTO(Integer id, String sender, String body, Long sentAt) {
        this.id = id;
        this.sender = sender;
        this.body = body;
        this.sentAt = sentAt;
    }

    public static MessageResponseDTO build (
        Message message
    ) {
        return new MessageResponseDTO(
            message.getId(),
            message.getUser().getUsername(),
            message.getBody(),
            message.getSentAt()
        );
    }

}
