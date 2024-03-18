package vsla.meeting.meeting.MeetingInterval;

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
@RequestMapping("/api/v1/meeting-intervals")
@Tag(name = "MeetingsInterval-Api")
public class MeetingIntervalController {
     private final MeetingIntervalFeignClient meetingIntervalFeignClient;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    @GetMapping("/getAll/App")
    public ResponseEntity<?> getAllMeetingIntervals() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        return meetingIntervalFeignClient.getAllMeetingIntervals(loggedInUser.getOrganization().getOrganizationId());
    }
}
