package es.us.dp1.lIng_04_25_26.endofline.developers;

import es.us.dp1.lIng_04_25_26.endofline.dto.PageDTO;
import org.apache.maven.model.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/developers")
public class DevelopersController {
    private final DeveloperService developerService;

    @Autowired
    public DevelopersController(
        DeveloperService developerService
    ) {
        this.developerService = developerService;
    }

    @GetMapping
    public ResponseEntity<PageDTO<Developer>> getDevelopers(
        @PageableDefault Pageable pageable
    ) {
        PageDTO<Developer> response = new PageDTO<>(developerService.getDevelopers(pageable));
        return ResponseEntity.ok(response);
    }

}
