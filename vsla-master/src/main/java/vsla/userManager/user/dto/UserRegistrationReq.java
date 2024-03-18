package vsla.userManager.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationReq {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, message = "Full name must be at least 2 characters")
    private String fullName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(?i)(MALE|FEMALE)$", message = "Gender Must be 'MALE' or 'FEMALE'")
    private String gender;

    private Boolean proxyEnabled;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "password is required")
    @Size(min = 4, max = 6, message = "password must be between 6 and 20 characters")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(?i)(USER|GROUP_ADMIN|ADMIN)$", message = "Role Must be 'ADMIN','GROUP_ADMIN'or 'USER'")
    private String roleName;

    @NotNull(message = "Company is required")
    private Long organizationId;
}