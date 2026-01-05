package es.us.dp1.lx_xy_24_25.endofline.card;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.card.CardNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

	private final CardRepository cardRepository;

	public CardService(
        CardRepository cardRepository
    ) {
		this.cardRepository = cardRepository;
    }

	@Transactional(readOnly = true)
	public List<Card> findAll() {
		return cardRepository.findAll();
	}

    @Transactional(readOnly = true)
    public Card findById(Integer id) {
        return cardRepository
            .findById(id)
            .orElseThrow(() -> new CardNotFoundException(id));
    }

	@Transactional(readOnly = true)
	public List<Card> getCardsByColor(String color) {
		Color actualColor = Color.valueOf(color);
		return cardRepository.findByColor(actualColor);
	}
}
