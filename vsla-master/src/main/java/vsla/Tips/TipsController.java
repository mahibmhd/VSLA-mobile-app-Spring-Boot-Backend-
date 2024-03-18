package vsla.Tips;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vsla.userManager.user.Users;
import vsla.utils.CurrentlyLoggedInUser;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/Tips")
public class TipsController {
    private final TipsFeignClient tipsFeignClient;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;

    @GetMapping("/getTips/App")
    public ResponseEntity<?> getAllTips() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        return tipsFeignClient.getAllTips(loggedInUser.getOrganization().getOrganizationId());
    }
}
