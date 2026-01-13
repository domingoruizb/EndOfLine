package es.us.dp1.lIng_04_25_26.endofline.gameplayercard;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.card.Card;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCardRepository;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCardService;

@ExtendWith(MockitoExtension.class)
class GamePlayerCardServiceTests {

    @Mock
    private GamePlayerCardRepository gpcRepository;

    @InjectMocks
    private GamePlayerCardService gpcService;

    private GamePlayerCard gpc;
    private GamePlayer gamePlayer;
    private Card card;
    private Game game;

    @BeforeEach
    void setup() {
        game = new Game();
        game.setId(99);

        gamePlayer = new GamePlayer();
        gamePlayer.setId(10);
        gamePlayer.setGame(game);

        card = new Card();
        card.setId(5);
        card.setImage("card.png");
        card.setInitiative(1);

        gpc = new GamePlayerCard();
        gpc.setId(1);
        gpc.setPositionX(3);
        gpc.setPositionY(2);
        gpc.setRotation(0);
        gpc.setPlacedAt(LocalDateTime.now());
        gpc.setGamePlayer(gamePlayer);
        gpc.setCard(card);
    }


    @Test
    void testSaveGamePlayerCard() {
        when(gpcRepository.save(any(GamePlayerCard.class))).thenReturn(gpc);
        
        GamePlayerCard saved = gpcService.save(gpc);
        
        assertNotNull(saved);
        assertEquals(1, saved.getId());
        assertEquals(3, saved.getPositionX());
        assertEquals(2, saved.getPositionY());
        verify(gpcRepository).save(gpc);
    }


    @Test
    void testFailWhenSavingNullGamePlayerCard() {
        when(gpcRepository.save(null)).thenThrow(new IllegalArgumentException("Entity cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> gpcService.save(null));
    }


    @Test
    void testGetLastPlacedCards() {
        when(gpcRepository.findLastPlacedCards(gamePlayer.getId())).thenReturn(List.of(gpc));

        List<GamePlayerCard> results = gpcService.getLastPlacedCards(gamePlayer);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(gpc, results.get(0));
        verify(gpcRepository).findLastPlacedCards(gamePlayer.getId());
    }


    @Test
    void testGetLastPlacedCard_WhenCardsExist() {
        when(gpcRepository.findLastPlacedCards(gamePlayer.getId())).thenReturn(List.of(gpc));

        GamePlayerCard result = gpcService.getLastPlacedCard(gamePlayer);

        assertNotNull(result);
        assertEquals(gpc, result);
        verify(gpcRepository).findLastPlacedCards(gamePlayer.getId());
    }


    @Test
    void testGetLastPlacedCard_WhenNoCardsExist() {
        when(gpcRepository.findLastPlacedCards(gamePlayer.getId())).thenReturn(Collections.emptyList());

        GamePlayerCard result = gpcService.getLastPlacedCard(gamePlayer);

        assertNull(result);
        verify(gpcRepository).findLastPlacedCards(gamePlayer.getId());
    }


    @Test
    void testGetByGame() {
        when(gpcRepository.findByGameId(game.getId())).thenReturn(List.of(gpc));

        List<GamePlayerCard> results = gpcService.getByGame(game);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(game.getId(), results.get(0).getGamePlayer().getGame().getId());
        verify(gpcRepository).findByGameId(game.getId());
    }


    @Test
    void testGetInitiatives() {
        when(gpcRepository.findInitiatives(gamePlayer.getId())).thenReturn(List.of(1, 4, 2));

        List<Integer> initiatives = gpcService.getInitiatives(gamePlayer);

        assertNotNull(initiatives);
        assertEquals(3, initiatives.size());
        assertEquals(1, initiatives.get(0));
        verify(gpcRepository).findInitiatives(gamePlayer.getId());
    }

}