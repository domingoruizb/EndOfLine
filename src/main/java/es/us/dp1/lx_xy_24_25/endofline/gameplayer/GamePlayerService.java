package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GamePlayerService {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    public GamePlayerService(GamePlayerRepository gamePlayerRepository) {
        this.gamePlayerRepository = gamePlayerRepository;
    }

    @Transactional(readOnly = true)
    public GamePlayer findById(Integer id) {
        return gamePlayerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("GamePlayer", "Id", id));
    }

    @Transactional
    public GamePlayer updatePlayerColor(Integer gameId, Integer userId, String newColor) {
        GamePlayer gamePlayer = gamePlayerRepository.findByGameIdAndUserId(gameId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("GamePlayer", "GameId/UserId", gameId + "/" + userId));

        gamePlayer.setColor(Color.valueOf(newColor));

        return gamePlayerRepository.save(gamePlayer);
    }

    @Transactional(readOnly = true)
    public GamePlayer getById(Integer id) {
        return gamePlayerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("GamePlayer", "Id", id));
    }

    @Transactional(readOnly = true)
    public GamePlayer getGamePlayer(Integer gameId, Integer userId) {
        return gamePlayerRepository.findByGameIdAndUserId(gameId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("GamePlayer", "GameId/UserId", gameId + "/" + userId));
    }

    @Transactional
    public void incrementCardsPlayedThisRound(Integer gamePlayerId) {
        GamePlayer gp = findById(gamePlayerId);

        gp.setCardsPlayedThisRound(gp.getCardsPlayedThisRound() + 1);
        gamePlayerRepository.save(gp);
    }

    @Transactional
    public GamePlayer updateGamePlayer(GamePlayer gamePlayer) {
        return gamePlayerRepository.save(gamePlayer);
    }

}
