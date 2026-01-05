package es.us.dp1.lx_xy_24_25.endofline.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("""
        SELECT m FROM Message m
        WHERE m.game.id = :gameId
        AND (:since IS NULL OR m.sentAt > :since)
        ORDER BY m.sentAt ASC
    """)
    List<Message> findMessages(Integer gameId, LocalDateTime since);
}
