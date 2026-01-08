package es.us.dp1.lIng_04_25_26.endofline.stats;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.game.GameRepository;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final GameRepository gameRepository;
    private final UserService userService;

    public StatsService(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public StatsDTO getStats() {
        User currentUser = userService.findCurrentUser();
        List<Game> allGames = (List<Game>) gameRepository.findAll();
        List<Game> finishedGames = allGames.stream()
                .filter(g -> g.getStartedAt() != null && g.getEndedAt() != null)
                .collect(Collectors.toList());

        StatsDTO.GlobalStats globalStats = calculateGlobalStats(finishedGames);
        StatsDTO.UserStats userStats = calculateUserStats(finishedGames, currentUser);

        return new StatsDTO(globalStats, userStats);
    }

    private StatsDTO.GlobalStats calculateGlobalStats(List<Game> finishedGames) {
        if (finishedGames.isEmpty()) {
            return new StatsDTO.GlobalStats(0, 0.0, 0, 0, 0L, 0.0, 0L, 0L, null, new ArrayList<>());
        }
        Map<Integer, Long> gamesPerPlayer = new HashMap<>();
        finishedGames.forEach(game ->
            game.getGamePlayers().forEach(gp -> {
                Integer userId = gp.getUser().getId();
                gamesPerPlayer.put(userId, gamesPerPlayer.getOrDefault(userId, 0L) + 1);
            })
        );

        Collection<Long> playerCounts = gamesPerPlayer.values();
        double avgGamesPerPlayer = playerCounts.isEmpty() ? 0.0 :
            playerCounts.stream().mapToLong(Long::longValue).average().orElse(0.0);
        int maxGamesByPlayer = playerCounts.isEmpty() ? 0 :
            playerCounts.stream().mapToInt(Long::intValue).max().orElse(0);
        int minGamesByPlayer = playerCounts.isEmpty() ? 0 :
            playerCounts.stream().mapToInt(Long::intValue).min().orElse(0);

        List<Long> durations = finishedGames.stream()
                .map(this::calculateDurationMinutes)
                .collect(Collectors.toList());

        long totalDuration = durations.stream().mapToLong(Long::longValue).sum();
        double avgDuration = durations.isEmpty() ? 0.0 :
            durations.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long maxDuration = durations.isEmpty() ? 0L :
            durations.stream().mapToLong(Long::longValue).max().orElse(0L);
        long minDuration = durations.isEmpty() ? 0L :
            durations.stream().mapToLong(Long::longValue).min().orElse(0L);

        String favoriteSkill = calculateGlobalFavoriteSkill(finishedGames);

        List<StatsDTO.PlayerRanking> rankings = calculatePlayerRankings(finishedGames);

        return new StatsDTO.GlobalStats(
                finishedGames.size(),
                avgGamesPerPlayer,
                maxGamesByPlayer,
                minGamesByPlayer,
                totalDuration,
                avgDuration,
                maxDuration,
                minDuration,
                favoriteSkill,
                rankings
        );
    }

    private StatsDTO.UserStats calculateUserStats(List<Game> finishedGames, User user) {
        List<Game> userGames = finishedGames.stream()
                .filter(g -> g.getGamePlayers().stream()
                        .anyMatch(gp -> gp.getUser().getId().equals(user.getId())))
                .collect(Collectors.toList());

        if (userGames.isEmpty()) {
            return new StatsDTO.UserStats(0, 0L, 0.0, 0L, 0L, null, 0, 0);
        }

        List<Long> durations = userGames.stream()
                .map(this::calculateDurationMinutes)
                .collect(Collectors.toList());

        long totalDuration = durations.stream().mapToLong(Long::longValue).sum();
        double avgDuration = durations.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long maxDuration = durations.stream().mapToLong(Long::longValue).max().orElse(0L);
        long minDuration = durations.stream().mapToLong(Long::longValue).min().orElse(0L);

        String favoriteSkill = calculateUserFavoriteSkill(userGames, user);

        int wins = (int) userGames.stream()
                .filter(g -> g.getWinner() != null && g.getWinner().getId().equals(user.getId()))
                .count();

        int losses = (int) userGames.stream()
                .filter(g -> g.getWinner() != null && !g.getWinner().getId().equals(user.getId()))
                .count();

        return new StatsDTO.UserStats(
                userGames.size(),
                totalDuration,
                avgDuration,
                maxDuration,
                minDuration,
                favoriteSkill,
                wins,
                losses
        );
    }

    private long calculateDurationMinutes(Game game) {
        if (game.getStartedAt() == null || game.getEndedAt() == null) {
            return 0L;
        }
        Duration duration = Duration.between(game.getStartedAt(), game.getEndedAt());
        return Math.max(0L, duration.toMinutes());
    }

    private String calculateGlobalFavoriteSkill(List<Game> games) {
        Map<Skill, Long> skillCounts = new HashMap<>();

        games.forEach(game ->
            game.getGamePlayers().forEach(gp ->
                gp.getSkillsUsed().forEach(skill ->
                    skillCounts.put(skill.getSkill(), skillCounts.getOrDefault(skill.getSkill(), 0L) + 1)
                )
            )
        );

        return skillCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse(null);
    }

    private String calculateUserFavoriteSkill(List<Game> games, User user) {
        Map<Skill, Long> skillCounts = new HashMap<>();

        games.forEach(game ->
            game.getGamePlayers().stream()
                .filter(gp -> gp.getUser().getId().equals(user.getId()))
                .forEach(gp ->
                    gp.getSkillsUsed().forEach(skill ->
                        skillCounts.put(skill.getSkill(), skillCounts.getOrDefault(skill.getSkill(), 0L) + 1)
                    )
                )
        );

        return skillCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse(null);
    }

    private List<StatsDTO.PlayerRanking> calculatePlayerRankings(List<Game> finishedGames) {
        Map<Integer, Integer> winsPerUser = new HashMap<>();
        Map<Integer, Integer> gamesPerUser = new HashMap<>();
        Map<Integer, String> usernameMap = new HashMap<>();

        finishedGames.forEach(game -> {
            if (game.getWinner() != null) {
                Integer winnerId = game.getWinner().getId();
                winsPerUser.put(winnerId, winsPerUser.getOrDefault(winnerId, 0) + 1);
                usernameMap.putIfAbsent(winnerId, game.getWinner().getUsername());
            }

            game.getGamePlayers().forEach(gp -> {
                Integer userId = gp.getUser().getId();
                gamesPerUser.put(userId, gamesPerUser.getOrDefault(userId, 0) + 1);
                usernameMap.putIfAbsent(userId, gp.getUser().getUsername());
            });
        });

        List<StatsDTO.PlayerRanking> rankings = gamesPerUser.entrySet().stream()
                .map(entry -> {
                    Integer userId = entry.getKey();
                    Integer games = entry.getValue();
                    Integer wins = winsPerUser.getOrDefault(userId, 0);
                    String username = usernameMap.get(userId);
                    return new StatsDTO.PlayerRanking(0, userId, username, wins, games);
                })
                .sorted(Comparator
                        .comparingInt(StatsDTO.PlayerRanking::getGamesWon).reversed()
                        .thenComparing(StatsDTO.PlayerRanking::getUsername))
                .collect(Collectors.toList());

        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        return rankings;
    }
}
