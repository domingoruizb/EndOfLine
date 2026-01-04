package es.us.dp1.lx_xy_24_25.endofline.stats;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
@Tag(name = "Statistics", description = "Game and player statistics API")
@SecurityRequirement(name = "bearerAuth")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public ResponseEntity<StatsDTO> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
