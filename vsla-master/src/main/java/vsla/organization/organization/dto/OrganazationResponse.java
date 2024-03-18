package vsla.organization.organization.dto;

import java.time.LocalDateTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import vsla.userManager.address.Address;
import vsla.userManager.user.dto.UserResponse;
import vsla.utils.Status;

@Data
public class OrganazationResponse {
    private Long organizationId;

    private String organizationName;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Status organizationStatus;

    private Address address;

    private UserResponse registeredBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
