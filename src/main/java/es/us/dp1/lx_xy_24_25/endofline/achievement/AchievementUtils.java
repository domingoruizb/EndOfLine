package es.us.dp1.lx_xy_24_25.endofline.achievement;

public class AchievementUtils {

    public static Boolean isUnlockable(Achievement achievement, long totalGamesPlayed, long totalWins, long totalDurationMinutes) {
        return switch (achievement.getCategory()) {
            case GAMES_PLAYED -> totalGamesPlayed >= achievement.getThreshold();
            case VICTORIES -> totalWins >= achievement.getThreshold();
            case TOTAL_PLAY_TIME -> totalDurationMinutes >= achievement.getThreshold();
        };
    }

}
