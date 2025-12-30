package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import es.us.dp1.lx_xy_24_25.endofline.achievement.Achievement;
import es.us.dp1.lx_xy_24_25.endofline.achievement.AchievementService;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerAchievementService {

    PlayerAchievementRepository repository;

    UserService userService;

    AchievementService achievementService;

    @Autowired
    public PlayerAchievementService(
        PlayerAchievementRepository repository,
        UserService userService,
        AchievementService achievementService
    ) {
        this.repository = repository;
        this.userService = userService;
        this.achievementService = achievementService;
    }

    @Transactional(readOnly = true)
    public Iterable<PlayerAchievement> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public PlayerAchievement findById(Integer id) throws ResourceNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PlayerAchievement", "id", id));
    }

    @Transactional(readOnly = true)
    public PlayerAchievement findByUserId (Integer user_id) {
        return repository.findByUserId(user_id);
    }

    @Transactional
    public PlayerAchievement create (
        Integer user_id,
        Integer achievement_id,
        LocalDateTime achievedAt
    ) throws ResourceNotFoundException {
        User user = userService.findUser(user_id);
        Achievement achievement = achievementService.getById(achievement_id);

        PlayerAchievement playerAchievement = new PlayerAchievement();

        playerAchievement.setUser(user);
        playerAchievement.setAchievement(achievement);
        playerAchievement.setAchievedAt(achievedAt);

        return repository.save(playerAchievement);
    }

    @Transactional
    public void delete (Integer id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean hasAchievement(Integer userId, Integer achievementId) {
        try {
            return repository.existsByUserIdAndAchievementId(userId, achievementId);
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerAchievement> findAllByUserId(Integer userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<PlayerAchievement> findAllByAchievementId(Integer achievementId) {
        return repository.findAllByAchievementId(achievementId);
    }

}
