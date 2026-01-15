package es.us.dp1.lIng_04_25_26.endofline.achievement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.us.dp1.lIng_04_25_26.endofline.authority.Authority;
import es.us.dp1.lIng_04_25_26.endofline.enums.Category;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.achievement.AchievementNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class AchievementServiceTests {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private jakarta.validation.Validator validator;

    @MockBean
    private es.us.dp1.lIng_04_25_26.endofline.user.UserService userService;

    @MockBean
    private es.us.dp1.lIng_04_25_26.endofline.playerachievement.PlayerAchievementService playerAchievementService;

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
    void testFindAllAchievements() {
        List<Achievement> achievements = this.achievementService.getAllAchievementsRaw();
        assertNotNull(achievements);
        assertFalse(achievements.isEmpty());
    }


    @Test
    void testFindAchievementById() {
        Achievement achievement = this.achievementService.getAchievementById(1);
        assertNotNull(achievement);
        assertEquals(1, achievement.getId());
    }


    @Test
    void testNotFindAchievementById() {
        assertThrows(AchievementNotFoundException.class, () -> this.achievementService.getAchievementById(404));
    }


    @Test
    @Transactional
    public void testInsertAchievement() {
        int count = this.achievementService.getAllAchievementsRaw().size();
        achievementService.saveAchievement(createTestAchievement());
        int count2 = this.achievementService.getAllAchievementsRaw().size();
        assertEquals(count + 1, count2);
    }


    @Test
    @Transactional
    public void testUpdateAchievement() {
        Achievement achievement = this.achievementService.getAchievementById(1);
        String oldName = achievement.getName();
        achievement.setName("Updated name");
        
        achievementService.updateAchievement(1, achievement);
        
        Achievement updated = this.achievementService.getAchievementById(1);
        assertEquals("Updated name", updated.getName());
        assertNotEquals(oldName, updated.getName());
    }


    @Test
    void testUpdateAchievementWithWrongId() {
        Achievement achievement = createTestAchievement();
        assertThrows(AchievementNotFoundException.class, () -> {
            achievementService.updateAchievement(999, achievement);
        });
    }


    @Test
    @Transactional
    public void testDeleteAchievement() {
        Achievement achievement = achievementService.saveAchievement(createTestAchievement());
        int count = this.achievementService.getAllAchievementsRaw().size();
        
        achievementService.deleteAchievement(achievement.getId());
        
        int count2 = this.achievementService.getAllAchievementsRaw().size();
        assertEquals(count - 1, count2);
    }


    @Test
    void testFailCreateAchievementWithInvalidData() {
        Achievement a = new Achievement();
        a.setName("");
        a.setThreshold(-10.0);

        var violations = validator.validate(a);
        assertFalse(violations.isEmpty(), "There should be validation violations");
    }


    @Test
    void testGetAllAchievementsAsAdmin() {
        User admin = new User();
        Authority auth = new Authority();
        auth.setType("ADMIN");
        admin.setAuthority(auth);
        
        when(userService.findCurrentUser()).thenReturn(admin);
        
        List<AchievementDTO> results = achievementService.getAllAchievements();
        
        assertNotNull(results);
        results.forEach(dto -> assertFalse(dto.isUnlocked(), "Admin should not see achievements as 'unlocked' individually in this view"));
    }

    @Test
    void testGetAllAchievementsAsPlayer() {
        User player = new User();
        player.setId(1);
        Authority auth = new Authority();
        auth.setType("PLAYER");
        player.setAuthority(auth);
        
        when(userService.findCurrentUser()).thenReturn(player);
        when(playerAchievementService.findAchievementIdsByUserId(1)).thenReturn(List.of(1));
        
        List<AchievementDTO> results = achievementService.getAllAchievements();
        
        assertNotNull(results);
        boolean foundUnlocked = results.stream()
            .anyMatch(dto -> dto.getId().equals(1) && dto.isUnlocked());
        
        assertTrue(foundUnlocked, "The achievement with ID 1 should appear as unlocked for this user");
    }

    @Test
    void testGetAllAchievementsAnonymous() {
        when(userService.findCurrentUser()).thenReturn(null);
        
        List<AchievementDTO> results = achievementService.getAllAchievements();
        
        assertNotNull(results);
        results.forEach(dto -> assertFalse(dto.isUnlocked()));
    }

    @Test
    @Transactional
    void testUnlockAchievementsLogic() {
        User user = new User();
        user.setId(1);
        
        when(playerAchievementService.hasAchievement(eq(1), anyInt())).thenReturn(false);
        
        achievementService.unlockAchievements(user, 1000, 1000, 1000);
        
        verify(playerAchievementService, atLeastOnce()).create(eq(user), any(Achievement.class), any());
    }

    @Test
    void testGetUnlockedAchievementIdsForUser() {
        Integer userId = 1;
        List<Integer> expectedIds = List.of(1, 2, 5);
        when(playerAchievementService.findAchievementIdsByUserId(userId)).thenReturn(expectedIds);
        
        List<Integer> actualIds = achievementService.getUnlockedAchievementIdsForUser(userId);
        
        assertEquals(expectedIds.size(), actualIds.size());
        assertTrue(actualIds.containsAll(expectedIds));
        verify(playerAchievementService).findAchievementIdsByUserId(userId);
    }

}