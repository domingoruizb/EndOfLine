package es.us.dp1.lIng_04_25_26.endofline.achievement;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/achievements")
@Tag(name = "Achievements", description = "The Achievements management API")
@SecurityRequirement(name = "bearerAuth")
public class AchievementController {

    private final AchievementService achievementService;


    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }


    @GetMapping
    public ResponseEntity<List<AchievementDTO>> getAllAchievements() {
        List<AchievementDTO> achievementDTOs = achievementService.getAllAchievements();
        return ResponseEntity.ok(achievementDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementDTO> getAchievementById(@PathVariable Integer id) {
        Achievement achievement = achievementService.getAchievementById(id);
        AchievementDTO dto = new AchievementDTO();
        dto.setId(achievement.getId());
        dto.setName(achievement.getName());
        dto.setDescription(achievement.getDescription());
        dto.setCategory(achievement.getCategory().toString());
        dto.setThreshold((int)achievement.getThreshold());
        dto.setUnlocked(false); // You may want to set this based on the current user
        dto.setBadgeImage(achievement.getBadgeImage());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AchievementDTO> createAchievement(@RequestBody @Valid Achievement newAchievement) {
        Achievement achievement = achievementService.saveAchievement(newAchievement);
        AchievementDTO dto = new AchievementDTO();
        dto.setId(achievement.getId());
        dto.setName(achievement.getName());
        dto.setDescription(achievement.getDescription());
        dto.setCategory(achievement.getCategory().toString());
        dto.setThreshold((int)achievement.getThreshold());
        dto.setUnlocked(false); // New achievements are not unlocked by default
        dto.setBadgeImage(achievement.getBadgeImage());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementDTO> updateAchievement(@RequestBody @Valid Achievement newAchievement, @PathVariable Integer id) {
        Achievement updatedAchievement = achievementService.updateAchievement(id, newAchievement);
        AchievementDTO dto = new AchievementDTO();
        dto.setId(updatedAchievement.getId());
        dto.setName(updatedAchievement.getName());
        dto.setDescription(updatedAchievement.getDescription());
        dto.setCategory(updatedAchievement.getCategory().toString());
        dto.setThreshold((int)updatedAchievement.getThreshold());
        dto.setUnlocked(false); // You may want to set this based on the current user
        dto.setBadgeImage(updatedAchievement.getBadgeImage());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(
        @PathVariable Integer id
    ) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.ok().build();
    }
}
