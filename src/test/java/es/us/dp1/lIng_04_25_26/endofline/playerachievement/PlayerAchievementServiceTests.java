package es.us.dp1.lIng_04_25_26.endofline.playerachievement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.achievement.Achievement;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

@ExtendWith(MockitoExtension.class)
class PlayerAchievementServiceTests {

    @Mock
    private PlayerAchievementRepository repository;

    @InjectMocks
    private PlayerAchievementService playerAchievementService;


    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(new PlayerAchievement()));

        Iterable<PlayerAchievement> result = playerAchievementService.findAll();

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        verify(repository).findAll();
    }


    @Test
    void testFindById() throws ResourceNotFoundException {
        PlayerAchievement pa = new PlayerAchievement();
        pa.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(pa));

        PlayerAchievement result = playerAchievementService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }


    @Test
    void testThrowExceptionWhenIdNotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerAchievementService.findById(999));
    }


    @Test
    void testFindByUserId() {
        PlayerAchievement pa = new PlayerAchievement();
        when(repository.findByUserId(1)).thenReturn(pa);

        PlayerAchievement result = playerAchievementService.findByUserId(1);

        assertNotNull(result);
        verify(repository).findByUserId(1);
    }


    @Test
    void testCreatePlayerAchievement() throws ResourceNotFoundException {
        User user = new User();
        user.setId(1);
        Achievement achievement = new Achievement();
        achievement.setId(100);
        LocalDateTime now = LocalDateTime.now();
        
        PlayerAchievement savedPa = PlayerAchievement.build(user, achievement, now);
        savedPa.setId(50);

        when(repository.save(any(PlayerAchievement.class))).thenReturn(savedPa);

        PlayerAchievement result = playerAchievementService.create(user, achievement, now);

        assertNotNull(result);
        assertEquals(50, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(achievement, result.getAchievement());
        verify(repository).save(any(PlayerAchievement.class));
    }


    @Test
    void testDeletePlayerAchievement() {
        playerAchievementService.delete(1);

        verify(repository).deleteById(1);
    }


    @Test
    void testCheckIfHasAchievementTrue() {
        when(repository.existsByUserIdAndAchievementId(1, 100)).thenReturn(true);

        boolean result = playerAchievementService.hasAchievement(1, 100);

        assertTrue(result);
    }


    @Test
    void testCheckIfHasAchievementFalse() {
        when(repository.existsByUserIdAndAchievementId(1, 100)).thenReturn(false);

        boolean result = playerAchievementService.hasAchievement(1, 100);

        assertFalse(result);
    }


    @Test
    void testReturnFalseIfCheckHasAchievementThrowsException() {
        when(repository.existsByUserIdAndAchievementId(anyInt(), anyInt())).thenThrow(new RuntimeException("DB Error"));

        boolean result = playerAchievementService.hasAchievement(1, 100);

        assertFalse(result);
    }


    @Test
    void testFindAllByUserId() {
        when(repository.findAllByUserId(1)).thenReturn(List.of(new PlayerAchievement()));

        List<PlayerAchievement> result = playerAchievementService.findAllByUserId(1);

        assertFalse(result.isEmpty());
        verify(repository).findAllByUserId(1);
    }


    @Test
    void testFindAchievementIdsByUserId() {
        Achievement achievement1 = new Achievement();
        achievement1.setId(101);
        
        Achievement achievement2 = new Achievement();
        achievement2.setId(102);

        PlayerAchievement pa1 = new PlayerAchievement();
        pa1.setAchievement(achievement1);
        
        PlayerAchievement pa2 = new PlayerAchievement();
        pa2.setAchievement(achievement2);

        when(repository.findAllByUserId(1)).thenReturn(List.of(pa1, pa2));

        List<Integer> result = playerAchievementService.findAchievementIdsByUserId(1);

        assertEquals(2, result.size());
        assertTrue(result.contains(101));
        assertTrue(result.contains(102));
    }


    @Test
    void testFindAllByAchievementId() {
        when(repository.findAllByAchievementId(100)).thenReturn(List.of(new PlayerAchievement()));

        List<PlayerAchievement> result = playerAchievementService.findAllByAchievementId(100);

        assertFalse(result.isEmpty());
        verify(repository).findAllByAchievementId(100);
    }

}