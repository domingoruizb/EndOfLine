package es.us.dp1.lIng_04_25_26.endofline.gameplayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.gameplayer.GamePlayerNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

import java.util.Optional;

@Service
public class GamePlayerService {

    private final GamePlayerRepository gamePlayerRepository;

    @Autowired
    public GamePlayerService(
        GamePlayerRepository gamePlayerRepository
    ) {
        this.gamePlayerRepository = gamePlayerRepository;
    }

    @Transactional
    public GamePlayer updatePlayerColor(Integer gameId, Integer userId, String unformattedColor) {
        String cleanedColor = unformattedColor.replace("\"", "").trim();
        GamePlayer gamePlayer = getGamePlayer(gameId, userId);
        gamePlayer.setColor(Color.valueOf(cleanedColor));

        return gamePlayerRepository.save(gamePlayer);
    }

    @Transactional(readOnly = true)
    public GamePlayer getById(Integer id) {
        return gamePlayerRepository.findById(id)
            .orElseThrow(GamePlayerNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Optional<GamePlayer> getGamePlayerAux(Integer gameId, Integer userId) {
        return gamePlayerRepository.findByGameIdAndUserId(gameId, userId);
    }

    @Transactional(readOnly = true)
    public GamePlayer getGamePlayer(Integer gameId, Integer userId) {
        return getGamePlayerAux(gameId, userId)
            .orElseThrow(() -> new GamePlayerNotFoundException(userId, gameId));
    }

    @Transactional(readOnly = true)
    public Boolean isSpectating(Game game, User user) {
        return getGamePlayerAux(game.getId(), user.getId()).isEmpty();
    }

    @Transactional(readOnly = true)
    public GamePlayer getGamePlayerOrFriend(Game game, User user) {
        GamePlayer possiblePlayer = getGamePlayerAux(game.getId(), user.getId())
            .orElse(null);

        GamePlayer gamePlayer = possiblePlayer == null ? getFriendInGame(user, game) : possiblePlayer;

        return gamePlayer;
    }

    @Transactional
    public void incrementCardsPlayedThisRound(GamePlayer gamePlayer) {
        gamePlayer.setCardsPlayedThisRound(gamePlayer.getCardsPlayedThisRound() + 1);
    }

    @Transactional(readOnly = true)
    public GamePlayer getOpponent (GamePlayer gamePlayer) {
        return gamePlayerRepository.findOpponent(gamePlayer.getGame().getId(), gamePlayer.getId())
            .orElseThrow(GamePlayerNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public GamePlayer getNextPlayer (Game game) {
        return gamePlayerRepository.findNextPlayer(game.getId(), game.getTurn())
            .orElseThrow(GamePlayerNotFoundException::new);
    }

    @Transactional
    public void resetCardsPlayedThisRound (GamePlayer gamePlayer) {
        gamePlayer.setCardsPlayedThisRound(0);
    }

    @Transactional(readOnly = true)
    public GamePlayer getFriendInGame(User user, Game game) {
        return gamePlayerRepository.findFriendsInGame(user.getId(), game.getId())
            .stream()
            .findFirst()
            .orElseThrow(GamePlayerNotFoundException::new);
    }

    @Transactional
    public GamePlayer updateGamePlayer(GamePlayer gamePlayer) {
        return gamePlayerRepository.save(gamePlayer);
    }

}
