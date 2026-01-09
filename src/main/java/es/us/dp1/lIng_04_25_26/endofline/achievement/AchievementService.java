package es.us.dp1.lIng_04_25_26.endofline.achievement;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.exceptions.achievement.AchievementNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.playerachievement.PlayerAchievementService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final PlayerAchievementService playerAchievementService;
    private final UserService userService;

    @Autowired
    public AchievementService(
        AchievementRepository achievementRepository,
        PlayerAchievementService playerAchievementService,
        UserService userService
    ) {
        this.achievementRepository = achievementRepository;
        this.playerAchievementService = playerAchievementService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Achievement> getAllAchievementsRaw() {
        return (List<Achievement>) achievementRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AchievementDTO> getAllAchievements() {
        Iterable<Achievement> achievements = achievementRepository.findAll();
        User user = userService.findCurrentUser();
        Boolean isAdmin = user != null && user.getAuthority().getType().equals("ADMIN");
        List<Integer> unlockedIds = (!isAdmin && user != null)
            ? playerAchievementService.findAchievementIdsByUserId(user.getId())
            : List.of();
        List<AchievementDTO> achievementDTOs =  ((List<Achievement>) achievements).stream().map(a -> {
            AchievementDTO dto = new AchievementDTO();
            dto.setId(a.getId());
            dto.setName(a.getName());
            dto.setDescription(a.getDescription());
            dto.setCategory(a.getCategory().toString());
            dto.setThreshold((int)a.getThreshold());
            dto.setUnlocked(!isAdmin && unlockedIds.contains(a.getId()));
            dto.setBadgeImage(a.getBadgeImage());
            return dto;
        }).collect(Collectors.toList());
        return achievementDTOs;
    }

    @Transactional(readOnly = true)
    public Achievement getAchievementById(Integer id) {
        return achievementRepository.findById(id)
                .orElseThrow(() -> new AchievementNotFoundException(id));
    }

    @Transactional
    public Achievement saveAchievement(@Valid Achievement newAchievement) {
        return achievementRepository.save(newAchievement);
    }

    @Transactional
    public Achievement updateAchievement(Integer id, Achievement achievement) {
        Achievement achievementToUpdate = getAchievementById(id);
        BeanUtils.copyProperties(achievement, achievementToUpdate, "id");
        return achievementRepository.save(achievementToUpdate);
    }

    @Transactional
    public void deleteAchievement(Integer id) {
        achievementRepository.deleteById(id);
    }

    @Transactional
    public void unlockAchievements(User user, long totalGamesPlayed, long totalWins, long totalDurationMinutes) {
        getAllAchievementsRaw()
            .stream()
            .filter(achievement -> !playerAchievementService.hasAchievement(user.getId(), achievement.getId()))
            .filter(achievement -> AchievementUtils.isUnlockable(achievement, totalGamesPlayed, totalWins, totalDurationMinutes))
            .forEach(achievement -> playerAchievementService.create(
                user,
                achievement,
                LocalDateTime.now()
            ));
    }

     @Transactional(readOnly = true)
    public List<Integer> getUnlockedAchievementIdsForUser(Integer userId) {
        return playerAchievementService.findAchievementIdsByUserId(userId);
    }

}
