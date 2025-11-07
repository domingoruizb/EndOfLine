package es.us.dp1.lx_xy_24_25.endofline.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
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
    private UserService userService;

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

    @Transactional(readOnly = true)
    public Game getGameByCode(String code) {
        return gameRepository.getGameByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Game", "code", code));
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final Random random = new Random();

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }

    @Transactional
    public Game createGame(Integer hostId) {
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", hostId));
        Game game = new Game();
        game.setRound(0);
        String newCode = generateRandomCode();
        game.setCode(newCode);
        game.setHost(host);
        game.setStartedAt(LocalDateTime.now());
        game.setEndedAt(null);
        GamePlayer hostGamePlayer = new GamePlayer();
        hostGamePlayer.setUser(host);
        hostGamePlayer.setGame(game);
        game.setGamePlayers(new ArrayList<>());
        game.getGamePlayers().add(hostGamePlayer);
        return gameRepository.save(game);
    }

    @Transactional
    public Game startGame(Integer id) {
        Game game = getGameById(id);
        if (game.getGamePlayers().size() != 2) {
            throw new IllegalStateException("Game needs to have 2 players");
        }
        if (game.getRound() != 0) {
            throw new IllegalStateException("Round needs to be 0");
        }
        game.setRound(1);
        game.setStartedAt(LocalDateTime.now());
        // Simple first turn: host starts
        game.setTurn(game.getHost().getId());
        return gameRepository.save(game);
    }

    @Transactional
    public Game joinGameByCode(Integer userId, String code) {
        Game game = getGameByCode(code);
        User player = userService.findUser(userId);
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setUser(player);
        gamePlayer.setGame(game);
        game.getGamePlayers().add(gamePlayer);
        return gameRepository.save(game);
    }

//    @Transactional
//    public Game endGame(Integer gameId, Integer winnerId) {
//        Game game = getGameById(gameId);
//        User winner = userRepository.findById(winnerId)
//                .orElseThrow(() -> new RuntimeException("Winner not found"));
//        game.endGame(winner);
//        return gameRepository.save(game);
//    }

    @Transactional
    public void deleteGame(Integer id) {
        gameRepository.deleteById(id);
    }

    @Transactional
    public Game nextTurn(Integer id) {
        Game game = getGameById(id);
        List<GamePlayer> players = game.getGamePlayers();
        if (players == null || players.isEmpty()) {
            throw new IllegalStateException("There are no players in the match.");
        }
        // Find index of current turn
        Integer currentTurnUserId = game.getTurn();
        int index = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUser().getId().equals(currentTurnUserId)) {
                index = i;
                break;
            }
        }
        int nextIndex = (index + 1) % players.size();
        game.setTurn(players.get(nextIndex).getUser().getId());
        return gameRepository.save(game);
    }

    @Transactional
    public Game endGame(Integer gameId, Integer winnerId) {
        Game game = getGameById(gameId);
        User winner = userRepository.findById(winnerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", winnerId));
        game.setWinner(winner);
        game.setEndedAt(LocalDateTime.now());
        return gameRepository.save(game);
    }

}