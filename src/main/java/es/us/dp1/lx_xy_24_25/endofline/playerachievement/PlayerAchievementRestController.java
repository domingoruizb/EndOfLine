package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/playerachievements")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "PlayerAchievement", description = "API for the management of PlayerAchievements")
public class PlayerAchievementRestController {

    private final PlayerAchievementService playerAchievementService;

    @Autowired
    public PlayerAchievementRestController(PlayerAchievementService service) {
        this.playerAchievementService = service;
    }

    @GetMapping
    public ResponseEntity<List<PlayerAchievementDTO>> findAll() {
        List<PlayerAchievementDTO> playerAchievements = ((List<PlayerAchievement>) playerAchievementService.findAll())
            .stream()
            .map(PlayerAchievementDTO::new)
            .toList();
        return new ResponseEntity<>(playerAchievements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlayerAchievementDTO> findById(@PathVariable Integer id) {
        PlayerAchievement playerAchievement = playerAchievementService.findById(id);
        return new ResponseEntity<>(new PlayerAchievementDTO(playerAchievement), HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlayerAchievementDTO> create (
        @RequestBody @Valid PlayerAchievementDTO playerAchievementDTO
    ) {
        PlayerAchievement playerAchievement = playerAchievementService.create(
            playerAchievementDTO.getUser_id(),
            playerAchievementDTO.getAchievement_id(),
            playerAchievementDTO.getAchieved_at() != null ? playerAchievementDTO.getAchieved_at() : LocalDateTime.now()
        );
        return new ResponseEntity<>(new PlayerAchievementDTO(playerAchievement), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlayerAchievement> delete (
        @PathVariable Integer id
    ) {
        playerAchievementService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
