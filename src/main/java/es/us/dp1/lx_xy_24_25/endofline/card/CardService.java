package es.us.dp1.lx_xy_24_25.endofline.card;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

	private final CardRepository cardRepository;

	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Transactional(readOnly = true)
	public List<Card> findAll() {
		return cardRepository.findAll();
	}

    @Transactional(readOnly = true)
    public Card findByImage(String image) {
        String path = "/cardImages/" + image + ".png";
        Card card = cardRepository.findByImage(path);

        if (card == null) {
            throw new ResourceNotFoundException("Card with image " + image + " not found");
        }

        return card;
    }

	@Transactional(readOnly = true)
	public List<Card> getCardsByColor(String color) {
		Color actualColor = Color.valueOf(color);
		return cardRepository.findByColor(actualColor);
	}
}
