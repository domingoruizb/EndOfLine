package es.us.dp1.lIng_04_25_26.endofline.achievement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lIng_04_25_26.endofline.achievement.Achievement;
import es.us.dp1.lIng_04_25_26.endofline.achievement.AchievementService;
import es.us.dp1.lIng_04_25_26.endofline.enums.Category;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class AchievementServiceTests {

    @Autowired
    private AchievementService achievementService;

    private Achievement createAchievement() {
        Achievement achievement = new Achievement();
        achievement.setName("Test achievement");
        achievement.setDescription("This is a test achievement");
        achievement.setBadgeImage("https://example.com/badgeImage.jpg");
        achievement.setThreshold(100.0);
        achievement.setCategory(Category.GAMES_PLAYED);
        return this.achievementService.saveAchievement(achievement);
    }

    @Test
    void findAllAchievementsTest() {
        List<Achievement> achievements = this.achievementService.getAchievements();
        assertEquals(9, achievements.size());
    }

    @Test
    void findAchievementByIdTest() {
        Achievement achievement = this.achievementService.getById(1);
        assertEquals("Rookie", achievement.getName());
    }

    @Test
    void notFindAchievementByIdTest() {
        assertThrows(ResourceNotFoundException.class, () -> this.achievementService.getById(404));
    }

    @Test
    @Transactional
    public void insertAchievementTest() {
        int count = this.achievementService.getAchievements().size();
        createAchievement();
        int count2 = this.achievementService.getAchievements().size();
        assertEquals(count + 1, count2);
    }

    @Test
    @Transactional
    public void updateAchievementTest() {
        Achievement achievement = this.achievementService.getById(1);
        achievement.setName("Updated name");
        achievement.setDescription("Updated description");
        achievementService.updateAchievement(1, achievement);
        Achievement updated = this.achievementService.getById(1);
        assertEquals("Updated name", updated.getName());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    void updateAchievementWithWrongIdTest() {
        Achievement achievement = new Achievement();
        achievement.setId(999);
        achievement.setName("X");
        achievement.setDescription("X");
        achievement.setCategory(Category.GAMES_PLAYED);
        achievement.setThreshold(5);
        assertThrows(ResourceNotFoundException.class, () -> {
            achievementService.updateAchievement(999, achievement);
        });
    }

    @Test
    @Transactional
    public void deleteAchievementTest() {
        Achievement achievement = createAchievement();
        int count = this.achievementService.getAchievements().size();
        achievementService.deleteAchievementById(achievement.getId());
        int count2 = this.achievementService.getAchievements().size();
        assertEquals(count - 1, count2);
    }

    @Test
    void deleteAchievementNonExistingTest() {
        assertDoesNotThrow(() -> {
            achievementService.deleteAchievementById(9999);
        });
        assertThrows(ResourceNotFoundException.class, () -> {
            achievementService.getById(9999);
        });
    }

    @Test
    void createAchievementWithInvalidDataShouldFail() {
        Achievement a = new Achievement();
        a.setName("Invalid Achievement");
        a.setDescription("");
        a.setCategory(null);
        a.setThreshold(-10);
        assertThrows(Exception.class, () -> {
            achievementService.saveAchievement(a);
        });
    }

    @Test
    void findByNameExistingTest() {
        Achievement achievement = achievementService.getAchievementByName("Rookie");
        assertNotNull(achievement);
        assertEquals(1, achievement.getId());
    }

    @Test
    void findByNameNonExistingTest() {
        Achievement achievement = achievementService.getAchievementByName("DOES_NOT_EXIST");
        assertNull(achievement);
    }
}
