package es.us.dp1.lIng_04_25_26.endofline.playerachievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.achievement.Achievement;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlayerAchievementService {

    private final PlayerAchievementRepository repository;

    @Autowired
    public PlayerAchievementService(
        PlayerAchievementRepository repository
    ) {
        this.repository = repository;
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
        User user,
        Achievement achievement,
        LocalDateTime achievedAt
    ) throws ResourceNotFoundException {
        PlayerAchievement playerAchievement = PlayerAchievement.build(user, achievement, achievedAt);
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
    public List<Integer> findAchievementIdsByUserId(Integer userId) {
        List<PlayerAchievement> achievements = repository.findAllByUserId(userId);
        return achievements.stream()
                .map(pa -> pa.getAchievement().getId())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlayerAchievement> findAllByAchievementId(Integer achievementId) {
        return repository.findAllByAchievementId(achievementId);
    }

}
