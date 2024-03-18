package vsla.LoanReason;

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
@RequestMapping("/api/v1/loanReason")
@Tag(name = "LoanReason-Api")
public class LoanReasonController {
     private final LoanReasonFeignClient loanReasonFeignClient;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllMeetingIntervals() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        return loanReasonFeignClient.getLoanReasonsByOrg(loggedInUser.getOrganization().getOrganizationId());
    }

    @GetMapping("/getLoanableFund")
    public ResponseEntity<?> getLoanableFund() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        return loanReasonFeignClient.getLoanReasonsByOrg(loggedInUser.getOrganization().getOrganizationId());
    }
}
