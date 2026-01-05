package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GamePlayerCardService {

    private final GamePlayerCardRepository gamePlayerCardRepository;

    @Autowired
    public GamePlayerCardService(
        GamePlayerCardRepository gamePlayerCardRepository
    ) {
        this.gamePlayerCardRepository = gamePlayerCardRepository;
    }

    @Transactional(readOnly = true)
    public List<GamePlayerCard> getLastPlacedCards (GamePlayer gamePlayer) {
        return gamePlayerCardRepository.findLastPlacedCards(gamePlayer.getId());
    }

    @Transactional(readOnly = true)
    public GamePlayerCard getLastPlacedCard (GamePlayer gamePlayer) {
        List<GamePlayerCard> lastPlacedCards = getLastPlacedCards(gamePlayer);

        return lastPlacedCards.isEmpty() ? null : lastPlacedCards.getFirst();
    }

    @Transactional(readOnly = true)
    public List<GamePlayerCard> getByGame (Game game) {
        return gamePlayerCardRepository.findByGameId(game.getId());
    }

    @Transactional(readOnly = true)
    public List<Integer> getInitiatives (GamePlayer gamePlayer) {
        return gamePlayerCardRepository.findInitiatives(gamePlayer.getId());
    }

    @Transactional
    public GamePlayerCard save (GamePlayerCard gamePlayerCard) {
        return gamePlayerCardRepository.save(gamePlayerCard);
    }
}
