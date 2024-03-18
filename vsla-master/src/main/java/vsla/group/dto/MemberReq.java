package vsla.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberReq {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, message = "Full name must be at least 2 characters")
    private String fullName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(?i)(MALE|FEMALE)$", message = "Gender Must be 'MALE' or 'FEMALE'")
    private String gender;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "password is required")
    @Size(min = 4, max = 6, message = "password must be between 6 and 20 characters")
    private String password;

    private boolean proxyEnabled;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(?i)(USER|GROUP_ADMIN|ADMIN)$", message = "Role Must be 'ADMIN','GROUP_ADMIN'or 'USER'")
    private String roleName;
}

