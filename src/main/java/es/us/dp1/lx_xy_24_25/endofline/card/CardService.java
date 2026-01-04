package es.us.dp1.lx_xy_24_25.endofline.card;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

	private final CardRepository cardRepository;
    private final GamePlayerCardRepository gpcRepository;

	public CardService(
        CardRepository cardRepository,
        GamePlayerCardRepository gpcRepository
    ) {
		this.cardRepository = cardRepository;
	    this.gpcRepository = gpcRepository;
    }

	@Transactional(readOnly = true)
	public List<Card> findAll() {
		return cardRepository.findAll();
	}

    @Transactional(readOnly = true)
    public Card findById(Integer id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Card findByImage(String image) {
        Card card = cardRepository.findByImage(image);

        if (card == null) {
            throw new ResourceNotFoundException("Card with image " + image + " not found");
        }

        return card;
    }

    @Transactional(readOnly = true)
    public List<GamePlayerCard> findByGameId(Integer gameId) {
        return gpcRepository.findByGameId(gameId);
    }

	@Transactional(readOnly = true)
	public List<Card> getCardsByColor(String color) {
		Color actualColor = Color.valueOf(color);
		return cardRepository.findByColor(actualColor);
	}
}
