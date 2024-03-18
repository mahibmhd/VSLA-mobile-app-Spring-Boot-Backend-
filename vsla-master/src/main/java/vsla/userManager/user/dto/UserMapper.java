package vsla.userManager.user.dto;

import vsla.userManager.user.Users;

public class UserMapper {
    public static UserResponse toUserResponse(Users user) {

//        Group group = user.getGroup();
//        MeetingTypeResponse groupResponse = new MeetingTypeResponse();
//        if (group != null)
//            groupResponse = MeetingTypeResponse.builder()
//                    .groupId(group.getGroupId())
//                    .groupName(group.getGroupName())
//                    .build();

        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .phoneNumber(user.getUsername())
                .role(user.getRole().getRoleName())
                .company(user.getOrganization().getOrganizationName())
//                .group(groupResponse)
                .address(user.getAddress())
                .userStatus(user.getUserStatus())
                .lastLoggedIn(user.getLastLoggedIn())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static UserResponse toMiniUserResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .role(user.getRole().getRoleName())
                .build();
    }
}

