package es.us.dp1.lx_xy_24_25.endofline.card;

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

	@Transactional
	public Card saveCard(Card card) {
		card.setUpdatedAt(LocalDateTime.now());
		return cardRepository.save(card);
	}

	@Transactional
	public Card updateCard(Integer id, Card card) {
		Card existing = getById(id);
		BeanUtils.copyProperties(card, existing, "id", "gamePlayer");
		existing.setUpdatedAt(LocalDateTime.now());
		return cardRepository.save(existing);
	}

	@Transactional
	public void deleteById(Integer id) {
		if (!cardRepository.existsById(id)) {
			throw new ResourceNotFoundException("Card with id " + id + " not found");
		}
		cardRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Card> findByGamePlayerId(Integer gamePlayerId) {
		return cardRepository.findByGamePlayer_Id(gamePlayerId);
	}

	@Transactional(readOnly = true)
	public List<Card> findAll() {
		return (List<Card>) cardRepository.findAll();
	}

}
