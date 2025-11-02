package es.us.dp1.lx_xy_24_25.endofline.achievement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import es.us.dp1.lx_xy_24_25.endofline.achievement.Achievement;
import es.us.dp1.lx_xy_24_25.endofline.achievement.AchievementService;
import es.us.dp1.lx_xy_24_25.endofline.achievement.Category;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase
public class AchievementServiceTests {

    @Autowired
    private AchievementService achievementService;

    private Achievement createAchievement(){
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
        List<Achievement> achievements = (List<Achievement>) this.achievementService.getAchievements();
        assertEquals(3, achievements.size());
    }

    @Test
    void findAchievementByIdTest(){
        Achievement achievement = this.achievementService.getById(1);
        assertEquals("Principiante", achievement.getName());
    }

    @Test
    void notFindAchievementByIdTest(){
        assertThrows(ResourceNotFoundException.class, () -> this.achievementService.getById(404));
    }

    @Test
    @Transactional
    public void insertAchievementTest() {
        Iterable<Achievement> achievements = achievementService.getAchievements();
        long count = achievements.spliterator().getExactSizeIfKnown();
        createAchievement();
        Iterable<Achievement> achievements2 = achievementService.getAchievements();
        long count2 = achievements2.spliterator().getExactSizeIfKnown();
        assertEquals(count + 1, count2);
    } 

    @Test
    @Transactional
    public void updateAchievementTest() {
        Achievement achievement = this.achievementService.getById(1);
        achievement.setName("Updated name");
        achievement.setDescription("Updated description");
        achievementService.updateAchievement(1, achievement);
        assertEquals("Updated name", achievement.getName());
        assertEquals("Updated description", achievement.getDescription());
    } 

    @Test
    @Transactional
    public void deleteAchievementTest() {
        Achievement achievement = createAchievement();
        Iterable<Achievement> achievements = achievementService.getAchievements();
        long count = achievements.spliterator().getExactSizeIfKnown();
        achievementService.deleteAchievementById(achievement.getId());
        Iterable<Achievement> achievements2 = achievementService.getAchievements();
        long count2 = achievements2.spliterator().getExactSizeIfKnown();
        assertEquals(count - 1, count2);
    } 

}
