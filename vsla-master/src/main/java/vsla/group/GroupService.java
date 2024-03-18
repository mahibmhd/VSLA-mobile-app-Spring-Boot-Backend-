package vsla.group;

import vsla.group.dto.*;
import vsla.organization.project.Project;
import vsla.userManager.user.dto.UserResponse;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface GroupService {
    GroupResponse createGroup(GroupRegistrationReq groupReq);
    GroupResponse updateGroup(GroupRegistrationReq groupReq);
    UserResponse addMember(MemberReq memberReq);
    ShareAmount getShareAmount();
    SocialFundAmount getSocialFundAmount();
    UserResponse editMember(UpdateMemberReq updateMemberReq,Long userId);

    UserResponse deleteMember(Long userId);

    List<GroupResponse> getAllGroupsByOrganization(Long organizationId);
    List<GroupResponse> getAllGroupsByProject(Long projectId);

    GroupResponse myGroup();
    MemberResponse getAllGroupMembers(Long groupId);
    List<MembersDto> getMembers(Long groupId);
    Group closeRound();
    List<MembersDto> getMembersForSocial(Long groupId);
    List<Group> getGroupsByProject(Project project);
    Map<Month,Integer> getGroupsCreatedPerMonth(Long organizationId);
    //List<Group> getGroupData();
   // GroupData getGroupData();
}
