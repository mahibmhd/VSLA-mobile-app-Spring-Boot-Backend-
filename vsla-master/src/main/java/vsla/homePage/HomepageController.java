package vsla.homePage;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home-page")
@Tag(name = "Home Page API.")
public class HomepageController {
    private final HomepageService homepageService;

    public HomepageController(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    @GetMapping
    public ResponseEntity<HomepageResponse> getAllMeetingsByGroup() {
        return ResponseEntity.ok(homepageService.getHomepageResponse());
    }
}


