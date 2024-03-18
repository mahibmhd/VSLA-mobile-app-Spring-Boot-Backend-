package vsla.TermsAndConditions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vsla.userManager.user.Users;
import vsla.utils.CurrentlyLoggedInUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/TermsandConditions")
public class TermsandConditionsController { 

    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    private final TermsandConditionsFeignClient termsandConditionsFeignClient;

    @GetMapping("/getTermsandConditions/App")
    public ResponseEntity<?> getAllTermsAndConditions() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
      return termsandConditionsFeignClient.getAllTermsAndConditions(loggedInUser.getOrganization().getOrganizationId());

    
}
}
