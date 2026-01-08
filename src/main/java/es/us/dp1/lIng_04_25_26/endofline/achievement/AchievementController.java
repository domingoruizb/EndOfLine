package es.us.dp1.lIng_04_25_26.endofline.achievement;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.us.dp1.lIng_04_25_26.endofline.auth.payload.response.MessageResponse;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.achievement.AchievementDTO;

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
    public ResponseEntity<Achievement> getAchievementById(
        @PathVariable Integer id
    ) {
        Achievement achievement = achievementService.getAchievementById(id);
        return ResponseEntity.ok(achievement);
    }

    @PostMapping
    public ResponseEntity<Achievement> createAchievement(
        @RequestBody @Valid Achievement newAchievement
    ) {
        Achievement achievement = achievementService.saveAchievement(newAchievement);
        return ResponseEntity.ok(achievement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Achievement> updateAchievement(
        @RequestBody @Valid Achievement newAchievement,
        @PathVariable Integer id
    ) {
        Achievement updatedAchievement = achievementService.updateAchievement(id, newAchievement);
        return ResponseEntity.ok(updatedAchievement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(
        @PathVariable Integer id
    ) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.ok().build();
    }
}
