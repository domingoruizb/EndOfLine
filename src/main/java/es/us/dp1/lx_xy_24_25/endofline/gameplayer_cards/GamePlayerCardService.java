package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePlayerCardService {

    private final GamePlayerCardRepository gpcRepository;

    @Autowired
    public GamePlayerCardService(GamePlayerCardRepository gpcRepository) {
        this.gpcRepository = gpcRepository;
    }

    public GamePlayerCard placeCard(GamePlayerCard gpc) {
        return gpcRepository.save(gpc);
    }

    public GamePlayerCard getLastCard(GamePlayer gamePlayer) {
        return gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(gamePlayer.getId()).getFirst();
    }

    public List<GamePlayerCard> getCards(GamePlayer gamePlayer) {
        return gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(gamePlayer.getId());
    }
}
