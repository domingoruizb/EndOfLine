package es.us.dp1.lIng_04_25_26.endofline.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.model.BaseEntity;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message extends BaseEntity {

    @Column(name = "body", nullable = false, length = 2000)
    private String body;

    @Column(name = "created_at", nullable = false)
    private Long sentAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public static Message build(
        String body,
        Long sentAt,
        User user,
        Game game
    ) {
        Message message = new Message();
        message.setBody(body);
        message.setSentAt(sentAt);
        message.setUser(user);
        message.setGame(game);
        return message;
    }
}
