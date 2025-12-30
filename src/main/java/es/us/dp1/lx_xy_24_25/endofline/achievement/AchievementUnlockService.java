package es.us.dp1.lx_xy_24_25.endofline.achievement;

import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.playerachievement.PlayerAchievementService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AchievementUnlockService {

    private final AchievementService achievementService;
    private final PlayerAchievementService playerAchievementService;

    @Autowired
    public AchievementUnlockService(
            AchievementService achievementService,
            PlayerAchievementService playerAchievementService
    ) {
        this.achievementService = achievementService;
        this.playerAchievementService = playerAchievementService;
    }
    @Transactional
    public void checkAndUnlockAchievements(User user, long totalGamesPlayed, long totalWins, long totalDurationMinutes) {
        List<Achievement> achievements = (List<Achievement>) achievementService.getAchievements();

        for (Achievement achievement : achievements) {
            if (playerAchievementService.hasAchievement(user.getId(), achievement.getId())) {
                continue;
            }

            boolean shouldUnlock = false;

            if (achievement.getCategory() == Category.GAMES_PLAYED) {
                if (totalGamesPlayed >= achievement.getThreshold()) {
                    shouldUnlock = true;
                }
            }
            else if (achievement.getCategory() == Category.VICTORIES) {
                if (totalWins >= achievement.getThreshold()) {
                    shouldUnlock = true;
                }
            }
            else if (achievement.getCategory() == Category.TOTAL_PLAY_TIME) {
                if (totalDurationMinutes >= achievement.getThreshold()) {
                    shouldUnlock = true;
                }
            }

            if (shouldUnlock) {
                playerAchievementService.create(
                        user.getId(),
                        achievement.getId(),
                        LocalDateTime.now()
                );
            }
        }
    }
}
