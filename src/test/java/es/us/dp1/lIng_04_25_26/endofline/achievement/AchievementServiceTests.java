package es.us.dp1.lIng_04_25_26.endofline.achievement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lIng_04_25_26.endofline.enums.Category;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.achievement.AchievementNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class AchievementServiceTests {

        @Autowired
        private AchievementService achievementService;
    
        @Autowired
        private jakarta.validation.Validator validator;

        private Achievement createTestAchievement() {
            Achievement achievement = new Achievement();
            achievement.setName("Test achievement");
            achievement.setDescription("This is a test achievement");
            achievement.setBadgeImage("https://example.com/badgeImage.jpg");
            achievement.setThreshold(100.0);
            achievement.setCategory(Category.GAMES_PLAYED);
            return achievement;
        }
    
    
        @Test
        void findAllAchievementsTest() {
            List<Achievement> achievements = this.achievementService.getAllAchievementsRaw();
            assertNotNull(achievements);
            assertFalse(achievements.isEmpty());
        }


        @Test
        void findAchievementByIdTest() {
            Achievement achievement = this.achievementService.getAchievementById(1);
            assertNotNull(achievement);
            assertEquals(1, achievement.getId());
        }


        @Test
        void notFindAchievementByIdTest() {
            assertThrows(AchievementNotFoundException.class, () -> this.achievementService.getAchievementById(404));
        }


        @Test
        @Transactional
        public void insertAchievementTest() {
            int count = this.achievementService.getAllAchievementsRaw().size();
            achievementService.saveAchievement(createTestAchievement());
            int count2 = this.achievementService.getAllAchievementsRaw().size();
            assertEquals(count + 1, count2);
        }


        @Test
        @Transactional
        public void updateAchievementTest() {
            Achievement achievement = this.achievementService.getAchievementById(1);
            String oldName = achievement.getName();
            achievement.setName("Updated name");
            
            achievementService.updateAchievement(1, achievement);
            
            Achievement updated = this.achievementService.getAchievementById(1);
            assertEquals("Updated name", updated.getName());
            assertNotEquals(oldName, updated.getName());
        }


        @Test
        void updateAchievementWithWrongIdTest() {
            Achievement achievement = createTestAchievement();
            assertThrows(AchievementNotFoundException.class, () -> {
                achievementService.updateAchievement(999, achievement);
            });
        }


        @Test
        @Transactional
        public void deleteAchievementTest() {
            Achievement achievement = achievementService.saveAchievement(createTestAchievement());
            int count = this.achievementService.getAllAchievementsRaw().size();
            
            achievementService.deleteAchievement(achievement.getId());
            
            int count2 = this.achievementService.getAllAchievementsRaw().size();
            assertEquals(count - 1, count2);
        }


        @Test
        void createAchievementWithInvalidDataShouldFail() {
            Achievement a = new Achievement();
            a.setName("");
            a.setThreshold(-10.0);

            var violations = validator.validate(a);
            assertFalse(violations.isEmpty(), "There should be validation violations");
        }

}