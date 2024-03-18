package vsla.awareness;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import vsla.userManager.user.Users;
import vsla.utils.CurrentlyLoggedInUser;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/awareness")
@Tag(name = "Awareness-Api")
public class AwarnessController {
    private final AwarnessFeignClient awarnessFeignClient;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    @GetMapping("/by-group")
    public ResponseEntity<?> getAllMeetingIntervals() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        return awarnessFeignClient.getAllAwarenessByGroupId(loggedInUser.getGroup().getGroupId());
    }
}
