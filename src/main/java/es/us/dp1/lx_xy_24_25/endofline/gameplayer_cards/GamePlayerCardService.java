package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
