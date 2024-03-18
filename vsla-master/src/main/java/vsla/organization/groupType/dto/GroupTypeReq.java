package vsla.organization.groupType.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupTypeReq {

    @NotBlank(message = "Group Type Name is required")
    private String groupTypeName;

    @NotNull(message = "organizationId is required")
    private Long organizationId;
}