package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardRepository;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GamePlayerCardRepository gpcRepository;
    private final UserService userService;
    private final GamePlayerService gamePlayerService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final Random random = new Random();

    public GameService(
        GameRepository gameRepository,
        UserService userService,
        GamePlayerService gamePlayerService,
        GamePlayerCardRepository gpcRepository
    ) {
        this.gameRepository = gameRepository;
        this.gpcRepository = gpcRepository;
        this.userService = userService;
        this.gamePlayerService = gamePlayerService;
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
    public List<Game> getGamesByWinner(User winner) {
        return gameRepository.findByWinner(winner);
    }

    @Transactional(readOnly = true)
    public List<Game> getGamesByHost(User host) {
        return gameRepository.findByHost(host);
    }

    @Transactional
    public List<Game> saveGames(List<Game> games) {
        return (List<Game>) gameRepository.saveAll(games);
    }

    @Transactional
    public void deleteGame(Integer id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Game", "id", id);
        }
    }

    @Transactional(readOnly = true)
    public Game getGameByCode(String code) {
        return gameRepository.getGameByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Game", "code", code));
    }

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
        User host = userService.findUser(hostId);
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
    public Game joinGameByCode(Integer userId, String code) {
        Game game = getGameByCode(code);
        User player = userService.findUser(userId);

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setUser(player);
        gamePlayer.setGame(game);
        game.getGamePlayers().add(gamePlayer);
        return gameRepository.save(game);
    }

    private Game finalizeGame(Game game, User winner) {
        game.markAsEnded(winner);
        return gameRepository.save(game);
    }

    @Transactional
    public Game endGame(Integer gameId, Integer winnerId) {
        Game game = getGameById(gameId);
        User winner = userService.findUser(winnerId);
        return finalizeGame(game, winner);
    }

    @Transactional
    public Game giveUpOrLose(Integer gameId, Integer userId) {
        Game game = getGameById(gameId);

        User winner = game.getGamePlayers().stream()
            .map(GamePlayer::getUser)
            .filter(u -> !u.getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No opponent found"));

        return finalizeGame(game, winner);
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

        game.markAsStarted();
        game.setTurn(game.getHost().getId());
        return gameRepository.save(game);
    }

    @Transactional
    public void advanceTurn(Game game) {
        List<GamePlayer> players = game.getGamePlayers();

        if (players.size() != 2) return;

        GamePlayer currentPlayer = players.stream()
            .filter(gp -> gp.getUser().getId().equals(game.getTurn()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Current player not found."));

        int cardsPerTurnLimit = getCardsPerTurnLimit(game, currentPlayer);
        boolean isSkillUsed = checkIfSkillUsedAndReset(game, currentPlayer.getCardsPlayedThisRound());
        if (isSkillUsed) {
            cardsPerTurnLimit = getCardsPerTurnLimit(game, currentPlayer);
        }

        int finalCardsPerTurnLimit = cardsPerTurnLimit;
        boolean allPlayersFinished = players.stream()
            .allMatch(gp -> gp.getCardsPlayedThisRound() >= finalCardsPerTurnLimit);

        if (allPlayersFinished) {
            startNextRound(game, players);
        } else {
            advanceToNextPlayer(game, players);
        }

        gameRepository.save(game);
    }

    private int getCardsPerTurnLimit(Game game, GamePlayer player) {
        if (game.getSkill() != null && game.getSkill() == Skill.SPEED_UP && player.getUser().getId().equals(game.getTurn())) {
        }
        return game.getRound() == 1 ? 1 : 2;
    }

    private void startNextRound(Game game, List<GamePlayer> players) {
        game.setRound(game.getRound() + 1);
        players.forEach(gp -> gp.setCardsPlayedThisRound(0));
        game.setSkill(null);

        if (game.getRound() > 1) {
            game.setTurn(determineNextTurnByInitiative(game));
        } else {
            game.setTurn(game.getHost().getId());
        }
    }

    private void advanceToNextPlayer(Game game, List<GamePlayer> players) {
        Integer nextPlayerId = players.stream()
            .filter(gp -> !gp.getUser().getId().equals(game.getTurn()))
            .findFirst()
            .map(gp -> gp.getUser().getId())
            .orElseThrow(() -> new IllegalStateException("Missing opponent in game"));

        game.setSkill(null);
        game.setTurn(nextPlayerId);
    }

    private boolean checkIfSkillUsedAndReset(Game game, int cardsPlayedAfterPlacement) {
        if (game.getSkill() != null) {
            int skillLimit;

            if (game.getSkill() == Skill.SPEED_UP) {
                skillLimit = 3;
            } else if (game.getSkill() == Skill.BRAKE) {
                skillLimit = 1;
            } else {
                return false;
            }

            if (cardsPlayedAfterPlacement >= skillLimit) {
                game.setSkill(null);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Game setUpSkill(Integer gameId, Integer userId, String skill) {
        Game game = getGameById(gameId);

        GamePlayer gamePlayer = game.getGamePlayers().stream()
            .filter(gp -> gp.getUser().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User is not a player in this game"));

        if (gamePlayer.getEnergy() <= 0) {
            throw new IllegalStateException("Not enough energy to set up skill");
        }

        gamePlayer.setEnergy(gamePlayer.getEnergy() - 1);
        game.setSkill(Skill.valueOf(skill));
        gamePlayerService.updateGamePlayer(gamePlayer);

        return gameRepository.save(game);
    }

    private Integer determineNextTurnByInitiative(Game game) {
        List<GamePlayer> players = game.getGamePlayers();
        GamePlayer player1 = players.get(0);
        GamePlayer player2 = players.get(1);

        GamePlayerCard lastCard1 = getLastCard(player1);
        GamePlayerCard lastCard2 = getLastCard(player2);

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
                return comparePreviousCardsRecursively(player1, player2);
            }
        }
    }

    private Integer comparePreviousCardsRecursively(GamePlayer gamePlayer1, GamePlayer gamePlayer2) {

        List<GamePlayerCard> cards1 = getCards(gamePlayer1);
        List<GamePlayerCard> cards2 = getCards(gamePlayer2);

        if (cards1.size() <= 1 && cards2.size() <= 1) {
            Game game = gamePlayer1.getGame();
            return game.getHost().getId();
        }

        int index = 1;

        while (true) {
            boolean hasCard1 = cards1.size() > index;
            boolean hasCard2 = cards2.size() > index;

            if (!hasCard1 && !hasCard2) {
                Game game = gamePlayer1.getGame();
                return game.getHost().getId();
            }

            if (hasCard1 && !hasCard2) {
                return cards1.size() > cards2.size() ?
                    cards2.get(0).getGamePlayer().getUser().getId() : cards1.get(0).getGamePlayer().getUser().getId();
            }

            if (!hasCard1 && hasCard2) {
                return cards2.size() > cards1.size() ?
                    cards1.get(0).getGamePlayer().getUser().getId() : cards2.get(0).getGamePlayer().getUser().getId();
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

    public GamePlayerCard getLastCard(GamePlayer gamePlayer) {
        return gpcRepository.findPlacedCards(gamePlayer.getId()).getFirst();
    }

    public List<GamePlayerCard> getCards(GamePlayer gamePlayer) {
        return gpcRepository.findPlacedCards(gamePlayer.getId());
    }
}
