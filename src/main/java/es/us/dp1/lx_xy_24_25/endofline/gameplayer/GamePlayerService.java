package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.enums.FriendStatus;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.friendship.Friendship;
import es.us.dp1.lx_xy_24_25.endofline.friendship.FriendshipService;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class GamePlayerService {

    private final GamePlayerRepository gamePlayerRepository;
    private final FriendshipService friendshipService;

    @Autowired
    public GamePlayerService(
        GamePlayerRepository gamePlayerRepository,
        FriendshipService friendshipService
    ) {
        this.gamePlayerRepository = gamePlayerRepository;
        this.friendshipService = friendshipService;
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
        return gamePlayerRepository.findByGameIdAndUserId(gameId, userId).orElse(null);
    }

    @Transactional
    public void incrementCardsPlayedThisRound(GamePlayer gamePlayer) {
        gamePlayer.setCardsPlayedThisRound(gamePlayer.getCardsPlayedThisRound() + 1);
        gamePlayerRepository.save(gamePlayer);
    }

    @Transactional
    public void setCardPlayedThisRoundTo0(GamePlayer gamePlayer) {
        gamePlayer.setCardsPlayedThisRound(0);
        gamePlayerRepository.save(gamePlayer);
    }

    public Boolean isValidTurn (
        GamePlayer gamePlayer
    ) {
        return gamePlayer.getGame().getTurn().equals(gamePlayer.getUser().getId());
    }

    public Boolean isHost (
        GamePlayer gamePlayer
    ) {
        return gamePlayer.getGame().getHost().getId().equals(gamePlayer.getUser().getId());
    }

    @Transactional(readOnly = true)
    public GamePlayer getOpponent(GamePlayer gamePlayer) {
        return gamePlayer.getGame().getGamePlayers()
            .stream()
            .filter(gp -> !gp.getId().equals(gamePlayer.getId()))
            .findFirst()
            .orElse(null);
    }

    @Transactional(readOnly = true)
    public GamePlayer getFriendInGame(User user, Game game) {
        Iterable<Friendship> friendships = friendshipService.findFriendshipsByUserId(
            user.getId(),
            FriendStatus.ACCEPTED
        );

        // Collect friend user IDs
        List<Integer> friendIds = StreamSupport.stream(friendships.spliterator(), false)
            .map(f -> f.getSender().getId().equals(user.getId()) ? f.getReceiver().getId() : f.getSender().getId())
            .toList();

        // Find a game player who is a friend
        return game.getGamePlayers()
            .stream()
            .filter(gp -> friendIds.contains(gp.getUser().getId()))
            .findFirst()
            .orElse(null);
    }

    @Transactional
    public GamePlayer updateGamePlayer(GamePlayer gamePlayer) {
        return gamePlayerRepository.save(gamePlayer);
    }

}
