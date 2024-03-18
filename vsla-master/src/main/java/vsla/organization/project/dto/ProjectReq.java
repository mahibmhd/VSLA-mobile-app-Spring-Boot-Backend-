package vsla.organization.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectReq {

    @NotBlank(message = "Project Name is required")
    private String projectName;

    @NotNull(message = "organizationId is required")
    private Long organizationId;
}