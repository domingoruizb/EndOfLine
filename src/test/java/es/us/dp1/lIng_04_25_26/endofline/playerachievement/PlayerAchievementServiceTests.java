package es.us.dp1.lIng_04_25_26.endofline.playerachievement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.playerachievement.PlayerAchievement;
import es.us.dp1.lIng_04_25_26.endofline.playerachievement.PlayerAchievementRepository;
import es.us.dp1.lIng_04_25_26.endofline.playerachievement.PlayerAchievementService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class PlayerAchievementServiceTests {

    @Autowired
    private PlayerAchievementService playerAchievementService;

    @Autowired
    private PlayerAchievementRepository repository;

    @BeforeEach
    void setup() {
        assertNotNull(playerAchievementService);
        assertNotNull(repository);
    }

    @Test
    void testFindAllAchievements() {
        List<PlayerAchievement> achievements =
                (List<PlayerAchievement>) playerAchievementService.findAll();

        achievements.forEach(pa -> {
            assertNotNull(pa.getUser());
            assertNotNull(pa.getAchievement());
            assertNotNull(pa.getAchievedAt());
        });
    }

    @Test
    void testFindAchievementById() {
        PlayerAchievement pa = playerAchievementService.findById(1);

        assertNotNull(pa);
        assertEquals(4, pa.getUser().getId());
        assertEquals(1, pa.getAchievement().getId());
    }

    @Test
    void testFindByIdNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> playerAchievementService.findById(99999));
    }

    @Test
    void testFindByUserId() {
        assertThrows(org.springframework.dao.IncorrectResultSizeDataAccessException.class,
            () -> playerAchievementService.findByUserId(4));
    }


    @Test
    void testFindByUserIdNoAchievement() {
        // User 1 no tiene achievements en tu SQL
        PlayerAchievement pa = playerAchievementService.findByUserId(1);
        assertNull(pa);
    }

    @Test
    void testCreateAndDeleteAchievement() {
        PlayerAchievement pa =
                playerAchievementService.create(4, 1, LocalDateTime.now());

        assertNotNull(pa);
        assertNotNull(pa.getId());
        assertEquals(4, pa.getUser().getId());
        assertEquals(1, pa.getAchievement().getId());

        Integer id = pa.getId();

        assertTrue(
                ((List<PlayerAchievement>) playerAchievementService.findAll())
                        .stream()
                        .anyMatch(x -> x.getId().equals(id))
        );

        playerAchievementService.delete(id);

        assertFalse(
                ((List<PlayerAchievement>) playerAchievementService.findAll())
                        .stream()
                        .anyMatch(x -> x.getId().equals(id))
        );
    }

    @Test
    void testCreateWithNullDate() {
        PlayerAchievement pa =
                playerAchievementService.create(4, 1, null);
        assertNotNull(pa);
        assertNull(pa.getAchievedAt());
        playerAchievementService.delete(pa.getId());
    }

    @Test
    void testCreateUserNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> playerAchievementService.create(99999, 1, LocalDateTime.now()));
    }

    @Test
    void testCreateAchievementNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> playerAchievementService.create(4, 99999, LocalDateTime.now()));
    }

    @Test
    void testDeleteNonExisting() {
        assertDoesNotThrow(() -> playerAchievementService.delete(99999));
    }
}
