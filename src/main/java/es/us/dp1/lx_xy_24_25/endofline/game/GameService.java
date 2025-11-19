package es.us.dp1.lx_xy_24_25.endofline.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerRepository;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardRepository;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
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
    private GamePlayerCardRepository gpcRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

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

    @Transactional
    public Game giveUpOrLose(Integer gameId, Integer userId) {
        Game game = getGameById(gameId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        game.setWinner(game.getGamePlayers().stream()   
                .map(GamePlayer::getUser)
                .filter(u -> !u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No opponent found")));
        game.setEndedAt(LocalDateTime.now());
        return gameRepository.save(game);
    }

    private Integer comparePreviousCardsRecursively(Integer gamePlayerId1, Integer gamePlayerId2) {
    
    List<GamePlayerCard> cards1 = gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(gamePlayerId1);

    List<GamePlayerCard> cards2 = gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(gamePlayerId2);
    
    if (cards1.size() <= 1 && cards2.size() <= 1) {
        Game game = gamePlayerRepository.findById(gamePlayerId1).get().getGame();
        return game.getHost().getId(); 
    }

    int index = 1; 

    while (true) {
        boolean hasCard1 = cards1.size() > index;
        boolean hasCard2 = cards2.size() > index;

        if (!hasCard1 && !hasCard2) {
            Game game = gamePlayerRepository.findById(gamePlayerId1).get().getGame();
            return game.getHost().getId(); 
        }

        if (hasCard1 && !hasCard2) {
            return cards1.size() > cards2.size() ? cards2.get(0).getGamePlayer().getUser().getId() : cards1.get(0).getGamePlayer().getUser().getId(); 
        }
        
        if (!hasCard1 && hasCard2) {
            return cards2.size() > cards1.size() ? cards1.get(0).getGamePlayer().getUser().getId() : cards2.get(0).getGamePlayer().getUser().getId();
        }

        Integer initiative1 = cards1.get(index).getCard().getInitiative();
        Integer initiative2 = cards2.get(index).getCard().getInitiative();

        if (initiative1 < initiative2) {
            return cards1.get(index).getGamePlayer().getUser().getId(); 
        } else if (initiative2 < initiative1) {
            return cards2.get(index).getGamePlayer().getUser().getId();
        }
        index++;
    }
}

    private Integer determineNextTurnByInitiative(Game game) {
    List<GamePlayer> players = game.getGamePlayers();
    GamePlayer player1 = players.get(0);
    GamePlayer player2 = players.get(1);

    GamePlayerCard lastCard1 = gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(player1.getId()).stream().findFirst().orElse(null);
    GamePlayerCard lastCard2 = gpcRepository.findByGamePlayerIdOrderByPlacedAtDesc(player2.getId()).stream().findFirst().orElse(null);

    if (lastCard1 == null || lastCard2 == null) {
        return game.getHost().getId();
    }

    Integer initiative1 = lastCard1.getCard().getInitiative();
    Integer initiative2 = lastCard2.getCard().getInitiative();

    if (initiative1 < initiative2) {
        return player1.getUser().getId(); 
    } else if (initiative2 < initiative1) {
        return player2.getUser().getId();
    } else {

        if (game.getRound() == 1) {
            return game.getHost().getId(); 
        } else {
            return comparePreviousCardsRecursively(player1.getId(), player2.getId());
        }
    }
}

    @Transactional
    public void advanceTurn(Integer gameId) {   
        Game game = getGameById(gameId);
            
        List<GamePlayer> players = game.getGamePlayers();

        if (players.size() != 2) {
            return; 
        }

        int cardsPerTurnLimit = game.getRound() == 1 ? 1 : 2;

        boolean allPlayersFinished = players.stream()
            .allMatch(gp -> gp.getCardsPlayedThisRound() >= cardsPerTurnLimit);

        if (allPlayersFinished) {
            game.setRound(game.getRound() + 1);
            players.forEach(gp -> gp.setCardsPlayedThisRound(0));
            
            if (game.getRound() > 1) { 
                game.setTurn(determineNextTurnByInitiative(game));
            } else {
                game.setTurn(game.getHost().getId());
            }
        } else {
            if (game.getRound() == 1) {
                Integer nextPlayerId = players.stream()
                    .filter(gp -> !gp.getUser().getId().equals(game.getTurn()))
                    .findFirst()
                    .map(gp -> gp.getUser().getId())
                    .orElseThrow(() -> new IllegalStateException("Missing opponent in game"));

                game.setTurn(nextPlayerId);
            } else {
                for (GamePlayer gp : players) {
                    if (!gp.getUser().getId().equals(game.getTurn()) && gp.getCardsPlayedThisRound() < cardsPerTurnLimit) {
                        game.setTurn(gp.getUser().getId());
                        break;
                    }
            }
        }

        game.setSkill(null);
        
        gameRepository.save(game);
    }

    }

    @Transactional
    public Game setUpSkill(Game game, GamePlayer gamePlayer, String skill) {
        if (gamePlayer.getEnergy() <= 0) {
            throw new IllegalStateException("Not enough energy to set up skill");
        }

        gamePlayer.setEnergy(gamePlayer.getEnergy() - 1);
        game.setSkill(Skill.valueOf(skill));
        return gameRepository.save(game);
    }

}