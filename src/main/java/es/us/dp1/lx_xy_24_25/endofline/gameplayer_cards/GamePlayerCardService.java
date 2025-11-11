package es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards;

import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GamePlayerCardService {

    private final GamePlayerCardRepository repository;

    public GamePlayerCardService(GamePlayerCardRepository repository) {
        this.repository = repository;
    }

    public List<GamePlayerCardDTO> getVisibleCards(Integer gameId, Integer viewerGamePlayerId) {
        return repository.findVisibleByGameIdForViewer(gameId, viewerGamePlayerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<GamePlayerCardDTO> getOnBoardCards(Integer gameId) {
        return repository.findOnBoardByGameId(gameId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private GamePlayerCardDTO toDto(GamePlayerCard gpc) {
        GamePlayerCardDTO dto = new GamePlayerCardDTO();
        dto.setId(gpc.getId());
        if (gpc.getCard() != null) {
            dto.setCardId(gpc.getCard().getId());
            // optional fields if they exist in your Card entity:
            // dto.setCardName(gpc.getCard().getName());
            // dto.setInitiative(gpc.getCard().getInitiative());
        }
        dto.setPositionX(gpc.getPositionX());
        dto.setPositionY(gpc.getPositionY());
        dto.setOnBoard(gpc.getOnBoard());
        dto.setPlacedAt(gpc.getPlacedAt());
        return dto;
    }
}
