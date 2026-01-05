package es.us.dp1.lx_xy_24_25.endofline.game;

import es.us.dp1.lx_xy_24_25.endofline.achievement.AchievementUnlockService;
import es.us.dp1.lx_xy_24_25.endofline.board.BoardUtils;
import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.game.GameNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.game.GameNotValidException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.gameplayer.GamePlayerNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.skill.SkillNotValidRequestException;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.user.UserNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.SkillUsage;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardRepository;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GamePlayerCardRepository gpcRepository;
    private final UserService userService;
    private final GamePlayerService gamePlayerService;
    private final AchievementUnlockService achievementUnlockService;
    private final GamePlayerCardService gamePlayerCardService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final Random random = new Random();

    public GameService(
        GameRepository gameRepository,
        UserService userService,
        GamePlayerService gamePlayerService,
        GamePlayerCardRepository gpcRepository,
        AchievementUnlockService achievementUnlockService,
        GamePlayerCardService gamePlayerCardService
    ) {
        this.gameRepository = gameRepository;
        this.gpcRepository = gpcRepository;
        this.userService = userService;
        this.gamePlayerService = gamePlayerService;
        this.achievementUnlockService = achievementUnlockService;
        this.gamePlayerCardService = gamePlayerCardService;
    }

    @Transactional(readOnly = true)
    public List<Game> findAll() {
        return (List<Game>) gameRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Game getGameById(Integer id) {
        return gameRepository.findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
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
    public void deleteGame(Game game) {
        gameRepository.deleteById(game.getId());
    }

    @Transactional(readOnly = true)
    public Game getGameByCode(String code) {
        return gameRepository.getGameByCode(code)
            .orElseThrow(() -> new GameNotFoundException(code));
    }

    private String generateRandomCode() {
        return random.ints(CODE_LENGTH, 0, CHARACTERS.length())
            .mapToObj(CHARACTERS::charAt)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }

    @Transactional
    public Game createGame(Integer hostId) {
        User host = userService.findUser(hostId);
        String newCode = generateRandomCode();
        Game game = Game.build(newCode, host);
        return gameRepository.save(game);
    }

    @Transactional
    public Game joinGameByCode(Integer userId, String code) {
        Game game = getGameByCode(code);
        User player = userService.findUser(userId);
        GamePlayer gamePlayer = GamePlayer.build(game, player);
        game.getGamePlayers().add(gamePlayer);
        return gameRepository.save(game);
    }

    private Game finalizeGame(Game game, User winner) {
        game.markAsEnded(winner);
        Game savedGame = gameRepository.save(game);

        for (GamePlayer gamePlayer : game.getGamePlayers()) {
            User player = gamePlayer.getUser();
            long totalGamesPlayed = gameRepository.countGamesPlayedByUser(player.getId());
            long totalWins = gameRepository.countGameWinsByUser(player.getId());
            long totalDurationMinutes = calculateTotalDurationMinutes(player.getId());

            achievementUnlockService.checkAndUnlockAchievements(
                    player,
                    totalGamesPlayed,
                    totalWins,
                    totalDurationMinutes
            );
        }

        return savedGame;
    }

    private long calculateTotalDurationMinutes(Integer userId) {
        List<Game> userGames = gameRepository.findFinishedGamesByUser(userId);
        return userGames.stream()
                .map(game -> {
                    if (game.getStartedAt() == null || game.getEndedAt() == null) {
                        return 0L;
                    }
                    Duration duration = Duration.between(game.getStartedAt(), game.getEndedAt());
                    return Math.max(0L, duration.toMinutes());
                })
                .mapToLong(Long::longValue)
                .sum();
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
            .orElseThrow(GamePlayerNotFoundException::new);

        return finalizeGame(game, winner);
    }

    @Transactional
    public Game startGame(Integer id) {
        Game game = getGameById(id);
        if (game.getGamePlayers().size() != 2) {
            throw new GameNotValidException("Game needs to have 2 players");
        }
        if (game.getRound() != 0) {
            throw new GameNotValidException("Round needs to be 0");
        }

        game.markAsStarted();
        game.setTurn(game.getHost().getId());
        return gameRepository.save(game);
    }

    @Transactional
    public void advanceTurn(Game game) {
        List<GamePlayer> players = game.getGamePlayers();

        if (players.size() != 2) return;

        Boolean allPlayersFinished = players.stream()
            .allMatch(BoardUtils::getIsTurnFinished);

        if (allPlayersFinished) {
            startNextRound(game, players);
        } else {
            advanceToNextPlayer(game, players);
        }
    }

    @Transactional
    public void startNextRound(Game game, List<GamePlayer> players) {
        game.setRound(game.getRound() + 1);
        players.forEach(gamePlayerService::resetCardsPlayedThisRound);
        game.setSkill(null);

        game.setTurn(game.getRound() > 1 ?
            determineNextTurnByInitiative(game) :
            game.getHost().getId()
        );
    }

    @Transactional
    public void advanceToNextPlayer(Game game, List<GamePlayer> players) {
        GamePlayer nextPlayer = gamePlayerService.getNextPlayer(game);

        game.setSkill(null);
        game.setTurn(nextPlayer.getUser().getId());
    }

    @Transactional
    public Game setUpSkill(Integer gameId, Integer userId, String skill) {
        Game game = getGameById(gameId);

        GamePlayer gamePlayer = game.getGamePlayers().stream()
            .filter(gp -> gp.getUser().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new UserNotFoundException(userId));

        if (gamePlayer.getEnergy() <= 0) {
            throw new SkillNotValidRequestException("Not enough energy to set up skill");
        }

        gamePlayer.setEnergy(gamePlayer.getEnergy() - 1);
        Skill skillEnum = Skill.valueOf(skill);
        SkillUsage skillUsage = SkillUsage.build(skillEnum, game.getRound());

        game.setSkill(skillEnum);
        gamePlayer.getSkillsUsed().add(skillUsage);

        gamePlayerService.updateGamePlayer(gamePlayer);

        return gameRepository.save(game);
    }

    private Integer determineNextTurnByInitiative(Game game) {
        List<GamePlayer> players = game.getGamePlayers();
        GamePlayer player1 = players.get(0);
        GamePlayer player2 = players.get(1);

        List<Integer> initiatives1 = gamePlayerCardService.getInitiatives(player1);
        List<Integer> initiatives2 = gamePlayerCardService.getInitiatives(player2);

        if (initiatives1.isEmpty() || initiatives2.isEmpty()) {
            return game.getHost().getId();
        }

        int minHistorySize = Math.min(initiatives1.size(), initiatives2.size());

        for (int i = 0; i < minHistorySize; i++) {
            Integer init1 = initiatives1.get(i);
            Integer init2 = initiatives2.get(i);

            if (init1 < init2) {
                return player1.getUser().getId();
            } else if (init2 < init1) {
                return player2.getUser().getId();
            }
        }

        // If all cards compared are equal or no cards exist, default to host
        return game.getHost().getId();
    }

}
