package es.us.dp1.lIng_04_25_26.endofline.achievement;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.us.dp1.lIng_04_25_26.endofline.auth.payload.response.MessageResponse;

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
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        return ResponseEntity.ok(achievements);
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
