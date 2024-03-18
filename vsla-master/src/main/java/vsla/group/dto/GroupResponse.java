package vsla.group.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vsla.group.Group;
import vsla.organization.groupType.GroupType;
import vsla.organization.project.Project;
import vsla.userManager.address.Address;
import vsla.userManager.user.Users;
import vsla.userManager.user.dto.UserMapper;
import vsla.userManager.user.dto.UserResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupResponse {

    private Long groupId;

    private String groupName;

    private Integer groupSize;

    private BigDecimal entryFee;

    private UserResponse groupAdmin;

    private Address address;

    private GroupType groupType;

    private Project project;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static GroupResponse toGroupResponse(Group group) {

        Users user = group.getGroupAdmin();
        UserResponse userResponse = UserMapper.toMiniUserResponse(user);

        return GroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .groupSize(group.getGroupSize())
                .entryFee(group.getEntryFee())
                .groupAdmin(userResponse)
                .address(group.getAddress())
                .groupType(group.getGroupType())
                .project(group.getProject())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();

    }
}
