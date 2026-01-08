package es.us.dp1.lx_xy_24_25.endofline.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import es.us.dp1.lIng_04_25_26.endofline.card.Card;
import es.us.dp1.lIng_04_25_26.endofline.card.CardRepository;
import es.us.dp1.lIng_04_25_26.endofline.card.CardService;
import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer_cards.GamePlayerCardRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTests {

    private CardRepository cardRepository;
    private GamePlayerCardRepository gpcRepository;
    private CardService cardService;

    @BeforeEach
    void setup() {
        cardRepository = mock(CardRepository.class);
        gpcRepository = mock(GamePlayerCardRepository.class);
        cardService = new CardService(cardRepository, gpcRepository);
    }

    @Test
    void testFindAll() {
        Card c1 = new Card();
        Card c2 = new Card();

        when(cardRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Card> result = cardService.findAll();

        assertEquals(2, result.size());
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void testFindByImageSuccess() {
        Card c = new Card();
        c.setImage("img1.png");

        when(cardRepository.findByImage("img1.png")).thenReturn(c);

        Card result = cardService.findByImage("img1.png");

        assertNotNull(result);
        assertEquals("img1.png", result.getImage());
    }

    @Test
    void testFindByImageNotFound() {
        when(cardRepository.findByImage("missing.png")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            cardService.findByImage("missing.png");
        });
    }

    @Test
    void testFindByGameId() {
        GamePlayerCard g1 = new GamePlayerCard();
        GamePlayerCard g2 = new GamePlayerCard();

        when(gpcRepository.findByGameId(10)).thenReturn(Arrays.asList(g1, g2));

        List<GamePlayerCard> result = cardService.findByGameId(10);

        assertEquals(2, result.size());
        verify(gpcRepository, times(1)).findByGameId(10);
    }

    @Test
    void testGetCardsByColor() {
        Card c1 = new Card();
        Card c2 = new Card();

        when(cardRepository.findByColor(Color.BLUE)).thenReturn(Arrays.asList(c1, c2));

        List<Card> result = cardService.getCardsByColor("BLUE");

        assertEquals(2, result.size());
        verify(cardRepository, times(1)).findByColor(Color.BLUE);
    }
}
