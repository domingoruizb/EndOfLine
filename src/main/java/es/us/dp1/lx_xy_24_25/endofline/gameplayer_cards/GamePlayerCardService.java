package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import es.us.dp1.lx_xy_24_25.endofline.game.GameService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerRepository;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePlayerCardService {

    private final GamePlayerCardRepository gamePlayerCardRepository;

    private final GamePlayerService gamePlayerService;
    private final GameService gameService;

    @Autowired
    public GamePlayerCardService(
        GamePlayerCardRepository gamePlayerCardRepository,
        GamePlayerService gamePlayerService,
        GameService gameService
    ) {
        this.gamePlayerCardRepository = gamePlayerCardRepository;
        this.gamePlayerService = gamePlayerService;
        this.gameService = gameService;
    }

    public GamePlayerCard placeCard(
        GamePlayerCard gamePlayerCard,
        Boolean isTurnFinished
    ) {
        GamePlayerCard saved = gamePlayerCardRepository.save(gamePlayerCard);

        gamePlayerService.incrementCardsPlayedThisRound(gamePlayerCard.getGamePlayer().getId());

        if (isTurnFinished) {
            gameService.advanceTurn(gamePlayerCard.getGamePlayer().getGame().getId());
        }

        return saved;
    }

    public GamePlayerCard getLastCard(GamePlayer gamePlayer) {
        return gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(gamePlayer.getId()).getFirst();
    }

    public List<GamePlayerCard> getCards(GamePlayer gamePlayer) {
        return gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(gamePlayer.getId());
    }
}
