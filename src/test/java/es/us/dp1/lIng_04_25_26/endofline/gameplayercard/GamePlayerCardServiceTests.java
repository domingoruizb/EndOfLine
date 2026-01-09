package es.us.dp1.lIng_04_25_26.endofline.gameplayercard;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.card.Card;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCardRepository;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCardService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GamePlayerCardServiceTests {

    @Mock
    private GamePlayerCardRepository gpcRepository;

    @Mock
    private GamePlayerService gamePlayerService;

    @InjectMocks
    private GamePlayerCardService gpcService;

    private GamePlayerCard gpc;
    private GamePlayer gamePlayer;
    private Card card;

    @BeforeEach
    void setup() {
        gamePlayer = new GamePlayer();
        gamePlayer.setId(10);

        card = new Card();
        card.setId(5);
        card.setImage("card.png");

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
    void shouldSaveGamePlayerCard() {
        when(gpcRepository.save(any(GamePlayerCard.class))).thenReturn(gpc);
        GamePlayerCard saved = gpcService.placeCard(gpc, false);
        assertNotNull(saved);
        assertEquals(1, saved.getId());
        assertEquals(3, saved.getPositionX());
        assertEquals(2, saved.getPositionY());
        assertEquals(0, saved.getRotation());
        assertEquals(gamePlayer, saved.getGamePlayer());
        assertEquals(card, saved.getCard());
        verify(gpcRepository).save(gpc);
    }

    @Test
    void shouldFailWhenSavingNullGamePlayerCard() {
        when(gpcRepository.save(null)).thenThrow(new IllegalArgumentException("Entity cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> gpcService.placeCard(null, false));
    }
}
