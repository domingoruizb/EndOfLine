package es.us.dp1.lx_xy_24_25.endofline.card;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.board.DeckNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.board.DeckNotValidRequestException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.card.CardNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CardService {

	private final CardRepository cardRepository;

	public CardService(
        CardRepository cardRepository
    ) {
		this.cardRepository = cardRepository;
    }

    @Transactional(readOnly = true)
    public Card getById(Integer id) {
        return cardRepository
            .findById(id)
            .orElseThrow(() -> new CardNotFoundException(id));
    }

	@Transactional(readOnly = true)
	public List<Card> getCardsByColor(String color) {
		Color actualColor = Color.valueOf(color);
		return getCardsByColor(actualColor);
	}

    @Transactional(readOnly = true)
    public List<Card> getCardsByColor(Color color) {
        return cardRepository.findByColor(color);
    }

    @Transactional(readOnly = true)
    public List<Card> getDeckCards (GamePlayer gamePlayer) {
        List<Integer> deckIds = gamePlayer.getDeckCards();
        List<Card> cards = cardRepository.findAllById(deckIds);

        Map<Integer, Card> cardMap = cards.stream()
            .collect(Collectors.toMap(Card::getId, card -> card));

        List<Card> orderedCards = deckIds.stream()
            .map(cardMap::get)
            .toList();

        return orderedCards;
    }

    @Transactional
    public List<Card> getRandomCardsByColor(Color color, Integer amount) {
        List<Card> cards = getCardsByColor(color);
        Collections.shuffle(cards);

        return cards.subList(0, amount);
    }

    @Transactional
    public void removeFromDeck (GamePlayer gamePlayer, Card card) {
        List<Integer> deck = gamePlayer.getDeckCards();
        if (!deck.contains(card.getId())) {
            throw new DeckNotFoundException(card.getId());
        }

        deck.remove(card.getId());

        gamePlayer.setDeckCards(deck);
    }

    @Transactional
    public void initializeDeck (GamePlayer gamePlayer) {
        Integer amount = GamePlayerUtils.isExtraGasEnabled(gamePlayer) ? 6 : 5;
        List<Card> cards = getRandomCardsByColor(gamePlayer.getColor(), amount);
        List<Integer> deck = cards.stream().map(Card::getId).collect(Collectors.toCollection(ArrayList::new));
        gamePlayer.setDeckCards(deck);
    }

    @Transactional
    public void refillDeck (GamePlayer gamePlayer) {
        Integer amount = GamePlayerUtils.isExtraGasEnabled(gamePlayer) ? 6 : 5;
        Integer refillSize = amount - gamePlayer.getDeckCards().size();

        if (refillSize <= 0) {
            return;
        }

        List<Integer> excludedIds = gamePlayer.getDeckCards();
        List<Card> availableCards = cardRepository.findAvailableCards(gamePlayer.getColor(), excludedIds);

        Collections.shuffle(availableCards);
        List<Integer> newDeck = availableCards.stream()
            .limit(refillSize)
            .map(Card::getId)
            .toList();

        List<Integer> updatedDeck = new ArrayList<>(gamePlayer.getDeckCards());
        updatedDeck.addAll(newDeck);
        gamePlayer.setDeckCards(updatedDeck);
    }

    @Transactional
    public void changeDeck (GamePlayer gamePlayer) {
        Integer round = gamePlayer.getGame().getRound();
        if (!gamePlayer.getCanRequestDeck() || round > 1) {
            throw new DeckNotValidRequestException(gamePlayer);
        }

        Integer amount = GamePlayerUtils.isExtraGasEnabled(gamePlayer) ? 6 : 5;
        List<Card> newCards = getRandomCardsByColor(gamePlayer.getColor(), amount);
        gamePlayer.setDeckCards(newCards.stream().map(Card::getId).toList());
        gamePlayer.setCanRequestDeck(false);
    }

}
