package vsla.group;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsla.group.dto.*;
import vsla.payment.Transaction.dto.SuccessResponse;
import vsla.userManager.user.Users;
import vsla.userManager.user.dto.UserResponse;
import vsla.utils.CurrentlyLoggedInUser;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Group Group API.")
public class GroupController {
    
    private final GroupService groupService;

    //public GroupController(GroupService groupService) {
      //  this.groupService = groupService;
    //}

    @GetMapping("/mine")
    public ResponseEntity<GroupResponse> getMe() {
        return ResponseEntity.ok(groupService.myGroup());
    }

    @GetMapping("/getShareAmount")
    public ShareAmount getShareAmount() {
        return groupService.getShareAmount();
    }
    @GetMapping("/getSocialFundAmount")
    public SocialFundAmount getSocialFundAmount() {
        return groupService.getSocialFundAmount();
    }
    @GetMapping("/by-organization/{organizationId}")
    public ResponseEntity<List<GroupResponse>> getAllGroups(@PathVariable Long organizationId) {
        return ResponseEntity.ok(groupService.getAllGroupsByOrganization(organizationId));
    }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<GroupResponse>> getAllGroupsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(groupService.getAllGroupsByProject(projectId));
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<MemberResponse> getAllGroupMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getAllGroupMembers(groupId));
    }

    @GetMapping("/{groupId}/constributors/roundPayment")
    public ResponseEntity<List<MembersDto>> getConconstributorsMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getMembers(groupId));
    }

    @GetMapping("/{groupId}/contributors/socialFund")
    public ResponseEntity<List<MembersDto>> getSocialFundMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getMembersForSocial(groupId));
    }

    @GetMapping("/getGroupsPerMonth/{organizationId}")
    public ResponseEntity<?> getGroupsPerMonth(@PathVariable Long organizationId) {
        return ResponseEntity.ok(groupService.getGroupsCreatedPerMonth(organizationId));
    }


    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody @Valid GroupRegistrationReq groupReq) {
        GroupResponse group = groupService.createGroup(groupReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @PutMapping
    public ResponseEntity<GroupResponse> updateGroup(@RequestBody @Valid GroupRegistrationReq groupReq) {
        return ResponseEntity.ok(groupService.updateGroup(groupReq));
    }

    @PostMapping("/add-member")
    public ResponseEntity<UserResponse> addMember(@RequestBody @Valid MemberReq memberReq) {
        return ResponseEntity.ok(groupService.addMember(memberReq));
    }

    @PutMapping("/edit-member/{userId}")
    public ResponseEntity<UserResponse> editMember(@RequestBody @Valid UpdateMemberReq updateMemberReq,
            @PathVariable Long userId) {
        return ResponseEntity.ok(groupService.editMember(updateMemberReq, userId));
    }

    @DeleteMapping("/delete-member/{userId}")
    public ResponseEntity<UserResponse> deleteMember(@PathVariable Long userId) {
        return ResponseEntity.ok(groupService.deleteMember(userId));
    }

    @PutMapping("/closeMeetingRound")
    public SuccessResponse ClooseMeeting() {
        groupService.closeRound();
        SuccessResponse successResponse= new SuccessResponse("Meeting Closed Successfully", "Success");
        return successResponse;
    }
}
