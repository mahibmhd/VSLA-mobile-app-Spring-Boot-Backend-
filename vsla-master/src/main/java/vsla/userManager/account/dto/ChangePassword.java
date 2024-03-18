package vsla.userManager.account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePassword {
    @Size(min = 4, message = "Password must be at least 6 characters long")
    @NotEmpty(message = "newPassword is required")
    private String newPassword;

    @NotEmpty(message = "oldPassword is required")
    private String oldPassword;
}
