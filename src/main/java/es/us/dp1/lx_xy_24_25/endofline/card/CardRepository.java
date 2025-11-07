package es.us.dp1.lx_xy_24_25.endofline.card;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {

    // Find cards by the gamePlayer's id
    List<Card> findByGamePlayer_Id(Integer gamePlayerId);

}
