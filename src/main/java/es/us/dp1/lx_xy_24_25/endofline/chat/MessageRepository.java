package es.us.dp1.lx_xy_24_25.endofline.chat;

import org.springframework.data.repository.CrudRepository;
import java.time.Instant;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    List<Message> findByGamePlayer_Game_IdOrderByCreatedAtAsc(Integer gameId);

    List<Message> findByGamePlayer_Game_IdAndCreatedAtAfterOrderByCreatedAtAsc(Integer gameId, Instant since);
}
