package vsla.userManager.account;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsla.userManager.account.dto.ChangePassword;
import vsla.userManager.account.dto.ResetPassword;
import vsla.utils.ApiResponse;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account API.")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping({"/change-password"})
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid ChangePassword changePassword) {
        return accountService.changePassword(changePassword);
    }

    @PutMapping({"/reset-password/{phoneNumber}"})
    public ResponseEntity<ApiResponse> changePassword(@PathVariable String phoneNumber, @RequestBody @Valid ResetPassword resetPassword) {
        return accountService.resetPassword(phoneNumber, resetPassword);
    }

}


