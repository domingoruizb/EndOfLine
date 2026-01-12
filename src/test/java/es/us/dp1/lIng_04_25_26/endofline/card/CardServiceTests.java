package es.us.dp1.lIng_04_25_26.endofline.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.board.DeckBadRequestException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.board.DeckNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.card.CardNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerUtils;

@ExtendWith(MockitoExtension.class)
class CardServiceTests {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private MockedStatic<GamePlayerUtils> gamePlayerUtilsMock;

    @BeforeEach
    void setup() {
        gamePlayerUtilsMock = mockStatic(GamePlayerUtils.class);
    }

    @AfterEach
    void tearDown() {
        gamePlayerUtilsMock.close();
    }


    @Test
    void testGetCardByIdSuccess() {
        Card card = new Card();
        card.setId(1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(cardRepository).findById(1);
    }

    @Test
    void testGetCardByIdNotFound() {
        when(cardRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(99));
    }

    @Test
    void testGetCardsByColorEnum() {
        Card c1 = new Card();
        c1.setColor(Color.RED);
        when(cardRepository.findByColor(Color.RED)).thenReturn(List.of(c1));

        List<Card> result = cardService.getCardsByColor(Color.RED);

        assertEquals(1, result.size());
        assertEquals(Color.RED, result.get(0).getColor());
    }

    @Test
    void testGetCardsByColorString() {
        Card c1 = new Card();
        c1.setColor(Color.BLUE);
        when(cardRepository.findByColor(Color.BLUE)).thenReturn(List.of(c1));

        List<Card> result = cardService.getCardsByColor("BLUE");

        assertEquals(1, result.size());
        verify(cardRepository).findByColor(Color.BLUE);
    }


    @Test
    void testGetDeckCards() {
        GamePlayer gp = mock(GamePlayer.class);
        List<Integer> deckIds = Arrays.asList(1, 2);
        
        Card c1 = new Card(); c1.setId(1);
        Card c2 = new Card(); c2.setId(2);

        when(gp.getDeckCards()).thenReturn(deckIds);
        when(cardRepository.findAllById(deckIds)).thenReturn(Arrays.asList(c1, c2));

        List<Card> result = cardService.getDeckCards(gp);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }


    @Test
    void testGetRandomCardsByColor() {
        Card c1 = new Card();
        Card c2 = new Card();
        Card c3 = new Card();
        when(cardRepository.findByColor(Color.RED)).thenReturn(new ArrayList<>(Arrays.asList(c1, c2, c3)));

        List<Card> result = cardService.getRandomCardsByColor(Color.RED, 2);

        assertEquals(2, result.size());
        verify(cardRepository).findByColor(Color.RED);
    }


    @Test
    void testRemoveFromDeckSuccess() {
        GamePlayer gp = mock(GamePlayer.class);
        List<Integer> currentDeck = new ArrayList<>(Arrays.asList(1, 2, 3));
        Card cardToRemove = new Card();
        cardToRemove.setId(2);

        when(gp.getDeckCards()).thenReturn(currentDeck);

        cardService.removeFromDeck(gp, cardToRemove);

        verify(gp).setDeckCards(anyList());
        assertFalse(currentDeck.contains(2));
    }


    @Test
    void testRemoveFromDeckNotFound() {
        GamePlayer gp = mock(GamePlayer.class);
        List<Integer> currentDeck = new ArrayList<>(Arrays.asList(1, 3));
        Card cardToRemove = new Card();
        cardToRemove.setId(2);

        when(gp.getDeckCards()).thenReturn(currentDeck);

        assertThrows(DeckNotFoundException.class, () -> cardService.removeFromDeck(gp, cardToRemove));
    }


    @Test
    void testInitializeDeckNormal() {
        GamePlayer gp = mock(GamePlayer.class);
        when(gp.getColor()).thenReturn(Color.BLUE);
        
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isExtraGasEnabled(gp)).thenReturn(false);

        List<Card> dummyCards = IntStream.range(0, 10).mapToObj(i -> {
            Card c = new Card(); c.setId(i); return c;
        }).collect(Collectors.toList());
        
        when(cardRepository.findByColor(Color.BLUE)).thenReturn(dummyCards);

        cardService.initializeDeck(gp);

        verify(gp).setDeckCards(org.mockito.ArgumentMatchers.argThat(list -> list.size() == 5));
    }


    @Test
    void testInitializeDeckExtraGas() {
        GamePlayer gp = mock(GamePlayer.class);
        when(gp.getColor()).thenReturn(Color.BLUE);
        
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isExtraGasEnabled(gp)).thenReturn(true);

        List<Card> dummyCards = IntStream.range(0, 10).mapToObj(i -> {
            Card c = new Card(); c.setId(i); return c;
        }).collect(Collectors.toList());

        when(cardRepository.findByColor(Color.BLUE)).thenReturn(dummyCards);

        cardService.initializeDeck(gp);

        verify(gp).setDeckCards(org.mockito.ArgumentMatchers.argThat(list -> list.size() == 6));
    }


    @Test
    void testRefillDeckAlreadyFull() {
        GamePlayer gp = mock(GamePlayer.class);
        List<Integer> fullDeck = Arrays.asList(1, 2, 3, 4, 5);
        when(gp.getDeckCards()).thenReturn(fullDeck);
        
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isExtraGasEnabled(gp)).thenReturn(false);

        cardService.refillDeck(gp);

        verify(cardRepository, times(0)).findAvailableCards(any(), any());
        verify(gp, times(0)).setDeckCards(any());
    }


    @Test
    void testRefillDeckNeeded() {
        GamePlayer gp = mock(GamePlayer.class);
        List<Integer> currentDeck = new ArrayList<>(Arrays.asList(1, 2));
        when(gp.getDeckCards()).thenReturn(currentDeck);
        when(gp.getColor()).thenReturn(Color.RED);
        
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isExtraGasEnabled(gp)).thenReturn(false);

        List<Card> available = new ArrayList<>();
        available.add(new Card()); available.get(0).setId(10);
        available.add(new Card()); available.get(1).setId(11);
        available.add(new Card()); available.get(2).setId(12);

        when(cardRepository.findAvailableCards(eq(Color.RED), eq(currentDeck))).thenReturn(available);

        cardService.refillDeck(gp);

        verify(gp).setDeckCards(org.mockito.ArgumentMatchers.argThat(list -> list.size() == 5 && list.contains(10)));
    }


    @Test
    void testChangeDeckSuccess() {
        GamePlayer gp = mock(GamePlayer.class);
        Game game = mock(Game.class);
        
        when(gp.getGame()).thenReturn(game);
        when(game.getRound()).thenReturn(1);
        when(gp.getCanRequestDeck()).thenReturn(true);
        when(gp.getColor()).thenReturn(Color.GREEN);
        
        gamePlayerUtilsMock.when(() -> GamePlayerUtils.isExtraGasEnabled(gp)).thenReturn(false);

        List<Card> dummyCards = IntStream.range(0, 10).mapToObj(i -> {
            Card c = new Card(); c.setId(i); return c;
        }).collect(Collectors.toList());
        when(cardRepository.findByColor(Color.GREEN)).thenReturn(dummyCards);

        cardService.changeDeck(gp);

        verify(gp).setDeckCards(org.mockito.ArgumentMatchers.argThat(list -> list.size() == 5));
        verify(gp).setCanRequestDeck(false);
    }


    @Test
    void testChangeDeckFailRound() {
        GamePlayer gp = mock(GamePlayer.class);
        Game game = mock(Game.class);
        
        when(gp.getGame()).thenReturn(game);
        when(game.getRound()).thenReturn(2);
        when(gp.getCanRequestDeck()).thenReturn(true);

        assertThrows(DeckBadRequestException.class, () -> cardService.changeDeck(gp));
    }

    
    @Test
    void testChangeDeckFailFlag() {
        GamePlayer gp = mock(GamePlayer.class);
        Game game = mock(Game.class);
        
        when(gp.getGame()).thenReturn(game);
        when(game.getRound()).thenReturn(1);
        when(gp.getCanRequestDeck()).thenReturn(false);

        assertThrows(DeckBadRequestException.class, () -> cardService.changeDeck(gp));
    }

}