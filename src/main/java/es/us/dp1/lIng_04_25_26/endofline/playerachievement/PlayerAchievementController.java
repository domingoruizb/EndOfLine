package es.us.dp1.lIng_04_25_26.endofline.playerachievement;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/playerachievements")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "PlayerAchievement", description = "API for the management of PlayerAchievements")
public class PlayerAchievementController {

    private final PlayerAchievementService playerAchievementService;
    private final UserService userService;

    @Autowired
    public PlayerAchievementController(PlayerAchievementService service, UserService userService) {
        this.playerAchievementService = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerAchievementDTO>> findAll() {
        User currentUser = userService.findCurrentUser();
        List<PlayerAchievementDTO> playerAchievements = playerAchievementService.findAllByUserId(currentUser.getId())
            .stream()
            .map(PlayerAchievementDTO::new)
            .toList();
        return ResponseEntity.ok(playerAchievements);
    }

    @GetMapping("/achievement/{achievementId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PlayerAchievementDTO>> findByAchievementId(
        @PathVariable Integer achievementId
    ) {
        List<PlayerAchievementDTO> playerAchievements = playerAchievementService.findAllByAchievementId(achievementId)
            .stream()
            .map(PlayerAchievementDTO::new)
            .toList();
        return ResponseEntity.ok(playerAchievements);
    }

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUnlockedAchievementIds() {
        User currentUser = userService.findCurrentUser();
        List<Integer> ids = playerAchievementService.findAchievementIdsByUserId(currentUser.getId());
        return ResponseEntity.ok(Map.of("ids", ids));
    }

}
