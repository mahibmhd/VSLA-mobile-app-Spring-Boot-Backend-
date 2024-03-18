package vsla.userManager.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import vsla.group.dto.GroupResponse;
import vsla.userManager.address.Address;
import vsla.userManager.user.UserStatus;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private Long userId;
    private String fullName;
    private String gender;
    private String phoneNumber;
    private String role;
    private String company;
    private GroupResponse group;
    private Address address;
    private UserStatus userStatus;
    private LocalDateTime lastLoggedIn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
