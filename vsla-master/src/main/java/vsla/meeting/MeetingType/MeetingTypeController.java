package vsla.meeting.MeetingType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vsla.userManager.user.Users;
import vsla.utils.CurrentlyLoggedInUser;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/meeting-types")
public class MeetingTypeController {
    private final MeetingTypeFeignClient meetingTypeFeignClient;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;

    @GetMapping("/getAll/App")
    public ResponseEntity<?> getAllMeetingIntervals() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        return meetingTypeFeignClient.getAllMeetingTypes(loggedInUser.getOrganization().getOrganizationId());
    }
}
