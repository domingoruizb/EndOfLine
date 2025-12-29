package es.us.dp1.lx_xy_24_25.endofline.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
    private GlobalStats global;
    private UserStats user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GlobalStats {
        private Integer totalFinishedGames;
        private Double avgGamesPerPlayer;
        private Integer maxGamesByPlayer;
        private Integer minGamesByPlayer;
        private Long totalDurationMinutes;
        private Double avgDurationMinutes;
        private Long maxDurationMinutes;
        private Long minDurationMinutes;
        private String favoriteSkill;
        private List<PlayerRanking> rankings;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStats {
        private Integer gamesPlayed;
        private Long totalDurationMinutes;
        private Double avgDurationMinutes;
        private Long maxDurationMinutes;
        private Long minDurationMinutes;
        private String favoriteSkill;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerRanking {
        private Integer rank;
        private Integer userId;
        private String username;
        private Integer gamesWon;
        private Integer gamesPlayed;
    }
}
