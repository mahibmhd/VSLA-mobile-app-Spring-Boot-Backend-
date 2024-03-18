package vsla.userManager.account;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vsla.userManager.account.dto.ChangePassword;
import vsla.userManager.account.dto.ResetPassword;
import vsla.utils.ApiResponse;

@Service
public interface AccountService {

    ResponseEntity<ApiResponse> changePassword(ChangePassword changePassword);

    ResponseEntity<ApiResponse> resetPassword(String phoneNumber, ResetPassword resetPassword);

}
