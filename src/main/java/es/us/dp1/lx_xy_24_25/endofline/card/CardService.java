package es.us.dp1.lx_xy_24_25.endofline.card;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardService {

	private final CardRepository cardRepository;

	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Transactional(readOnly = true)
	public Card getById(Integer id) {
		return cardRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Card with id " + id + " not found"));
	}

	@Transactional(readOnly = true)
	public List<Card> findAll() {
		return (List<Card>) cardRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Card> getCardsByColorLine(String color) {
		Color actualColor = Color.valueOf(color);
		return cardRepository.findByColorLine(actualColor);
	}

/*
	@Transactional(readOnly = true)
	public List<Card> getCardsByGamePlayer(Integer gamePlayerId) {
        return cardRepository.findByGamePlayerId(gamePlayerId);
    }

	@Transactional(readOnly = true)
	public List<Card> getOnBoardCards(Integer gameId) {
		return cardRepository.findOnBoardByGameId(gameId);
	}

	@Transactional(readOnly = true)
	public List<Card> getVisibleCards(Integer gameId, Integer viewerGamePlayerId) {
		// Visible cards for a player are the player's own cards plus those on the board
        List<Card> visibleCards = cardRepository.findOnBoardByGameId(gameId);
        visibleCards.addAll(cardRepository.findByGamePlayerId(viewerGamePlayerId));
        return visibleCards;
    }

	*/

}
