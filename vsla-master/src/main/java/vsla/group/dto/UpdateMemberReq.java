package vsla.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class UpdateMemberReq {
     @NotBlank(message = "Full name is required")
    @Size(min = 2, message = "Full name must be at least 2 characters")
    private String fullName;
    private Boolean proxyEnabled;
    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

}
