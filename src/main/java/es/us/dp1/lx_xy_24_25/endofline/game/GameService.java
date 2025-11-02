package es.us.dp1.lx_xy_24_25.endofline.game;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserRepository;


@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public Iterable<Game> findAll() {
        return gameRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Game getGameById(Integer id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", id));
    }

    @Transactional
    public Game createGame(Integer hostId) {
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", hostId));
        Game game = new Game();
        game.setRound(0);
        game.setHost(host);
        game.setStartedAt(null);
        game.setEndedAt(null);
        return gameRepository.save(game);
    }

    @Transactional
    public Game startGame(Integer id) {
        Game game = getGameById(id);
        if (game.getStartedAt() != null)
            throw new IllegalStateException("Game already started");

        game.setStartedAt(LocalDateTime.now());
        game.setRound(1);
        return gameRepository.save(game);
    }

    @Transactional
    public Game endGame(Integer gameId, Integer winnerId) {
        Game game = getGameById(gameId);
        User winner = userRepository.findById(winnerId)
                .orElseThrow(() -> new RuntimeException("Winner not found"));
        game.endGame(winner);
        return gameRepository.save(game);
    }

    @Transactional
    public void deleteGame(Integer id) {
        gameRepository.deleteById(id);
    }
/*
    @Transactional
    public Game nextTurn(Integer id) {
        Game game = getGameById(id);
        game.nextTurn();
        return gameRepository.save(game);
    }

    @Transactional
    public Game nextRound(Integer id) {
        Game game = getGameById(id);
        game.nextRound();
        return gameRepository.save(game);
    }
*/
}
