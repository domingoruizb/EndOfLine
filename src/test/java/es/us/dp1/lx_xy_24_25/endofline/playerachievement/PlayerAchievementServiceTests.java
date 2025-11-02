package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import es.us.dp1.lx_xy_24_25.endofline.achievement.Achievement;
import es.us.dp1.lx_xy_24_25.endofline.achievement.AchievementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class PlayerAchievementServiceTests {

    @Autowired
    private PlayerAchievementService playerAchievementService;

    @Test
    void testFindAllAchievements () {
        List<PlayerAchievement> playerAchievements = (List<PlayerAchievement>) this.playerAchievementService.findAll();

        playerAchievements.stream()
            .forEach(pa -> {
                assertNotNull(pa.getAchievement(), "Achievement should not be null");
                assertNotNull(pa.getUser(), "User should not be null");
                assertNotNull(pa.getAchievedAt(), "AchievedAt should not be null");
            });
    }

    @Test
    void testFindAchievementById () {
        PlayerAchievement playerAchievement = this.playerAchievementService.findById(1);

        assertNotNull(playerAchievement, "Achievement should not be null");
        assertNotNull(playerAchievement.getUser(), "User should not be null");
        assertNotNull(playerAchievement.getAchievement(), "Achievement should not be null");
        assertNotNull(playerAchievement.getAchievedAt(), "Achievement should not be null");
    }

    @Test
    void testCreateAndDeleteAchievement() {
        PlayerAchievement playerAchievement = this.playerAchievementService.create(1, 3, LocalDateTime.now());
        assertNotNull(playerAchievement, "Achievement should not be null");
        assertNotNull(playerAchievement.getId(), "Achievement id should not be null");
        assertNotNull(playerAchievement.getUser(), "User should not be null");
        assertNotNull(playerAchievement.getAchievement(), "Achievement should not be null");
        assertNotNull(playerAchievement.getAchievedAt(), "Achievement should not be null");

        Integer playerAchievementId = playerAchievement.getId();

        List<PlayerAchievement> playerAchievements = (List<PlayerAchievement>) this.playerAchievementService.findAll();
        assertTrue(playerAchievements.stream()
            .anyMatch(pa -> pa.getId().equals(playerAchievementId)));

        this.playerAchievementService.delete(playerAchievementId);

        playerAchievements = (List<PlayerAchievement>) this.playerAchievementService.findAll();
        assertFalse(playerAchievements.stream()
            .anyMatch(pa -> pa.getId().equals(playerAchievementId)));
    }

}
