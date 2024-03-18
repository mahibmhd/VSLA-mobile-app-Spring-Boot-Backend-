package vsla.group;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import vsla.Attendance.Attendace;
import vsla.Attendance.AttendaceRepository;
import vsla.Otp.dto.OtpException;
import vsla.Otp.dto.OtpResponse;
import vsla.exceptions.customExceptions.BadRequestException;
import vsla.exceptions.customExceptions.ResourceAlreadyExistsException;
import vsla.group.dto.*;
import vsla.organization.groupType.GroupType;
import vsla.organization.groupType.GroupTypeService;
import vsla.organization.organization.Organization;
import vsla.organization.project.Project;
import vsla.organization.project.ProjectService;
import vsla.payment.Transaction.Transaction;
import vsla.payment.Transaction.TransactionRepository;
import vsla.userManager.address.Address;
import vsla.userManager.address.AddressService;
import vsla.userManager.address.dto.AddressRegistrationReq;
import vsla.userManager.role.Role;
import vsla.userManager.role.RoleService;
import vsla.userManager.user.UserRepository;
import vsla.userManager.user.UserService;
import vsla.userManager.user.UserStatus;
import vsla.userManager.user.Users;
import vsla.userManager.user.dto.UserMapper;
import vsla.userManager.user.dto.UserResponse;
import vsla.utils.CurrentlyLoggedInUser;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupTypeService groupTypeService;
    private final ProjectService projectService;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    private final AddressService addressService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final AttendaceRepository attendaceRepository;
     @Value("${MESSAGE_API.BASE_URL}")
    private String messageApiBaseUrl;

    @Override
    @Transactional
    // @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    public GroupResponse createGroup(GroupRegistrationReq groupReq) {
        // Retrieve the currently logged-in user
        Users loggedInUser = currentlyLoggedInUser.getUser();
        Organization organization = loggedInUser.getOrganization();

        // Check if the user is already a member of a group
        if (loggedInUser.getGroup() != null)
            throw new IllegalArgumentException(
                    "You are currently a member of the '" + loggedInUser.getGroup().getGroupName() + "' group.");

        // Create an address for the group
        Address address = addressService.addAddress(groupReq.getAddress());

        // Get project and project type

        Project project = projectService.getProjectById(groupReq.getProjectId());
        GroupType groupType = groupTypeService.getGroupTypeById(groupReq.getGroupTypeId());

        // Create a new group
        Group group = new Group();
        group.setGroupAdmin(loggedInUser);
        group.setGroupName(groupReq.getGroupName());
        group.setGroupSize(groupReq.getGroupSize());
        group.setInterestRate(groupReq.getInterestRate());
        group.setShareAmount(groupReq.getShareAmount());
        group.setSocialFundAmount(groupReq.getSocialFundAmount());
        group.setEntryFee(groupReq.getEntryFee());
        group.setAddress(address);
        group.setCurrentRound(0);
        group.setIsActive(true);
        group.setProject(project);
        group.setGroupType(groupType);
        group.setOrganization(organization);
        loggedInUser.setAddress(address);
        userRepository.save(loggedInUser);
        group = groupRepository.save(group);

        // Update the user's group membership
        updateUser(loggedInUser, group);

        // // create default meeting for the group
        // meetingService.createDefaultMeeting(group, groupReq.getMeetingIntervalId(), groupReq.getMeetingDate());

        return GroupResponse.toGroupResponse(group);
    }

    private void updateUser(Users user, Group group) {
        user.setGroup(group);
        userRepository.save(user);
    }

    @Override
    public GroupResponse updateGroup(GroupRegistrationReq groupReq) {
        return null;
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public UserResponse addMember(MemberReq memberReq) {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        // Get the user's organization and group
        Organization organization = loggedInUser.getOrganization();
        Group group = loggedInUser.getGroup();
        if (group == null)
            throw new BadRequestException("You cannot add a member to a non-existing group.");

        // Check if the phone number is already taken
        if (userRepository.findByUsername(memberReq.getPhoneNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Phone number is already taken");

        // Retrieve the user's role
        Role role = roleService.getRoleByRoleName(memberReq.getRoleName());

        // Create an address for the new user
        AddressRegistrationReq addressRegistrationReq=new AddressRegistrationReq();
        addressRegistrationReq.setKebele(loggedInUser.getGroup().getAddress().getKebele());
        addressRegistrationReq.setRegion(loggedInUser.getGroup().getAddress().getRegion());
        addressRegistrationReq.setWoreda(loggedInUser.getGroup().getAddress().getWoreda());
        addressRegistrationReq.setZone(loggedInUser.getGroup().getAddress().getZone());
        Address address = addressService.addAddress(addressRegistrationReq);
        // Create a new user
        Users user = Users.builder()
                .username(memberReq.getPhoneNumber())
                .password(passwordEncoder.encode(memberReq.getPassword()))
                .fullName(memberReq.getFullName())
                .gender(memberReq.getGender())
                .hasPayedCurrentRound(false)
                .hasPayedCurrentSocialFund(false)
                .proxyEnabled(memberReq.isProxyEnabled())
                .role(role)
                .deleted(false)
                .group(group)
                .address(address)
                .organization(organization)
                .userStatus(UserStatus.ACTIVE)
                .build();

        // Save the new user to the database
        user = userRepository.save(user);
          try {
                String uri = messageApiBaseUrl;
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                // headers.setBearerAuth(res3.getBody().getToken());
                String requestBody = "{\"mobile\":" + "\"" + memberReq.getPhoneNumber() + "\"," + "\"text\":" + "\""
                        + "Dear "+ memberReq.getFullName()+" welcome. you can login using the following credentials. username: 0"+memberReq.getPhoneNumber()+" password: "+memberReq.getPassword() + "\"" + "}";
                System.out.println(requestBody);
                HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

                @SuppressWarnings("null")
                ResponseEntity<OtpResponse> res = restTemplate.exchange(uri, HttpMethod.POST, request,
                        OtpResponse.class);

               System.out.println(res);
            } catch (Exception e) {
                OtpException response = new OtpException("error", e.getMessage());
                System.out.println(e.getMessage());
            }
        // Map the user to a response object
        return UserMapper.toUserResponse(user);
    }

    @Override
    public List<GroupResponse> getAllGroupsByOrganization(Long organizationId) {
        List<Group> groups = groupRepository.findByOrganizationOrganizationId(organizationId);
        return groups.stream()
                .map(GroupResponse::toGroupResponse)
                .toList();
    }

    @Override
    public List<GroupResponse> getAllGroupsByProject(Long projectId) {
        List<Group> groups = groupRepository.findByProjectProjectId(projectId);
        return groups.stream()
                .map(GroupResponse::toGroupResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public MemberResponse getAllGroupMembers(Long groupId) {
        List<Users> users = userService.getUsersByGroup(groupId);
        List<Users> enabledUsers = new ArrayList<Users>();
        users.stream().forEach(u -> {
            if (u.getDeleted() == false) {
                enabledUsers.add(u);
            }
        });

        return MemberResponse.toResponse(enabledUsers);
    }

    @Override
    public GroupResponse myGroup() {
        Group group = currentlyLoggedInUser.getUser().getGroup();
        return GroupResponse.toGroupResponse(group);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public UserResponse editMember(UpdateMemberReq updateMemberReq, Long userId) {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        // Get the user's organization and group
        Group group = loggedInUser.getGroup();
        if (group == null)
            throw new BadRequestException("You cannot edit a member to a non-existing group.");

        // Check if the phone number is already taken
        Users memeberToBeEdited = userRepository.findUsersByUserId(userId);
        if (memeberToBeEdited == null) {
            throw new BadRequestException("member does not exist");
        } else {
            memeberToBeEdited.setUsername("");
            userRepository.save(memeberToBeEdited);
            if (userRepository.findByUsername(updateMemberReq.getPhoneNumber()).isPresent()) {
                throw new ResourceAlreadyExistsException("Phone number is already taken");
            } else {
                memeberToBeEdited.setFullName(updateMemberReq.getFullName());
                memeberToBeEdited.setUsername(updateMemberReq.getPhoneNumber());
                memeberToBeEdited.setProxyEnabled(updateMemberReq.getProxyEnabled());
                Users editedUser = userRepository.save(memeberToBeEdited);
                return UserMapper.toUserResponse(editedUser);
            }

        }

    }

    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public UserResponse deleteMember(Long userId) {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        // Get the user's organization and group
        Group group = loggedInUser.getGroup();
        if (group == null)
            throw new BadRequestException("You cannot delete a member to a non-existing group.");
        Users user = userRepository.findUsersByUserId(userId);
        if (user == null) {
            throw new BadRequestException("member does not exist");
        } else {
            user.setDeleted(true);
            return UserMapper.toUserResponse(user);
        }
    }
    Double maxAmount=0.0;

    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public List<MembersDto> getMembers(Long groupId) {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        List<MembersDto> members = new ArrayList<MembersDto>();
        List<Users> users = userRepository.findByGroupGroupId(groupId);
        users.stream().forEach(u -> {
            if (u.getDeleted() == false) {
                maxAmount=0.0;
                List<Transaction> transactions=transactionRepository.findTransactionByPayer(u);
                transactions.stream().forEach(ts->{
                    if(ts.getPaymentType().getPaymentTypeId()==1)
                    {
                        maxAmount+=ts.getAmount();
                    }
                });
                maxAmount=maxAmount*3;
                 int decimalPlaces = 2;

            // Create a DecimalFormat object with the desired pattern
            DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));

            // Format the double value to a string with the specified number of decimal
            // places
            String formatted = decimalFormat.format(maxAmount);
            // Parse the formatted string back into a double
             Double result = Double.parseDouble(formatted);
                MembersDto member = new MembersDto();
                member.setUserId(u.getUserId().toString());
                member.setFullName(u.getFullName());
                member.setGender(u.getGender());
                member.setProxy(u.getProxyEnabled().toString());
                member.setMaxAmount(result.toString());

                List<Attendace> attendaces=attendaceRepository.findAll();
                Optional<Integer> highestRound2=attendaces.stream()
                                    .filter(a->a.getUser().getGroup().getGroupId().compareTo(loggedInUser.getGroup().getGroupId())==0)
                                    .map(Attendace::getMeetingRound)
                                    .max(Integer::compare);
                                    int roundValue2=highestRound2.orElse(0);
                                    member.setRound(String.valueOf(roundValue2+1));
                members.add(member);
            }
        });
        return members;
    }
    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public List<MembersDto> getMembersForSocial(Long groupId) {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        List<MembersDto> members = new ArrayList<MembersDto>();
        List<Users> users = userRepository.findByGroupGroupId(groupId);
        users.stream().forEach(u -> {
            if (u.getDeleted() == false) {
                MembersDto member = new MembersDto();
                member.setUserId(u.getUserId().toString());
                member.setFullName(u.getFullName());
                member.setGender(u.getGender());
                member.setProxy(u.getProxyEnabled().toString());
                member.setHasPaidCurrentRound(u.getHasPayedCurrentRound().toString());
                member.setHasPaidCurrentSocialFund(u.getHasPayedCurrentSocialFund().toString());
                // List<Transaction> transactions = transactionRepository.findTransactionByPayer(u);

                // Optional<Integer> highestRound = transactions.stream()
                //         .filter(t->t.getPaymentType().getPaymentTypeId()==4)
                //         .map(Transaction::getRound)
                //         .max(Integer::compare);

                // int roundValue = highestRound.orElse(0); // provide a default value if the optional is empty
                member.setRound(String.valueOf(loggedInUser.getGroup().getCurrentRound()+1));

                members.add(member);
            }
        });
        return members;
    }

    @Override
    public List<Group> getGroupsByProject(Project project) {
        return groupRepository.findGroupByProject(project);
    }


  //  @Override
    //public GroupData getGroupData() {
      //  Users loggedInUser = currentlyLoggedInUser.getUser();
        //List<Group> groups = groupRepository.findByOrganizationOrganizationId(loggedInUser.getOrganization().getOrganizationId());
        //Map<Month, Integer> groupsPerMonth = getGroupsCreatedPerMonth();

        //return new GroupData(groups, groupsPerMonth);
    //}


    @Override
    public Map<Month, Integer> getGroupsCreatedPerMonth(Long organizationId) {
       Map<Month , Integer> groupsPerMonth = new HashMap<>();
      // System.out.println("Groups Created");
       List<Group> groups = groupRepository.findByOrganizationOrganizationId(organizationId);

       for (Group grooup : groups) {
        LocalDateTime createdAt = grooup.getCreatedAt();
        Month month = createdAt.getMonth();
        groupsPerMonth.put(month, groupsPerMonth.getOrDefault(month, 0) + 1);

       }

       return groupsPerMonth;
    }

    @Override
    public ShareAmount getShareAmount() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        ShareAmount shareAmount= new ShareAmount();
        shareAmount.setShareAmount(loggedInUser.getGroup().getShareAmount().toString());
        Group group=groupRepository.findByGroupId(loggedInUser.getGroup().getGroupId());
        int roundValue=group.getCurrentRound();


        List<Attendace> attendaces=attendaceRepository.findAll();
        Optional<Integer> highestRound2=attendaces.stream()
                            .filter(a->a.getUser().getGroup().getGroupId().compareTo(loggedInUser.getGroup().getGroupId())==0)
                            .map(Attendace::getMeetingRound)
                            .max(Integer::compare);
                            int roundValue2=highestRound2.orElse(0);
                            System.out.println("saving round "+roundValue);
                            System.out.println("attendance round "+roundValue2);
                            if(roundValue<roundValue2)
                            {
                                shareAmount.setIsAttendaceCompleted("1");
                            }
                            else{

                                shareAmount.setIsAttendaceCompleted("0");
                            }
                return shareAmount;
    }

    @Override
    public SocialFundAmount getSocialFundAmount() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        SocialFundAmount socialFundAmount= new SocialFundAmount();
        socialFundAmount.setSocialFundAmount(loggedInUser.getGroup().getSocialFundAmount().toString());
        // List<Transaction> transactions=transactionRepository.findTransactionByPaymentTypePaymentTypeId(4L);
        // Optional<Integer> highestRound = transactions.stream()
        //                 .filter(t->t.getPaymentType().getPaymentTypeId()==1&&t.getPayer().getGroup().getGroupId().compareTo(loggedInUser.getGroup().getGroupId())==0)
        //                 .map(Transaction::getRound)
        //                 .max(Integer::compare);
        //                 int roundValue = highestRound.orElse(0);
        Group group=groupRepository.findByGroupId(loggedInUser.getGroup().getGroupId());
        int roundValue=group.getCurrentRound();

        List<Attendace> attendaces=attendaceRepository.findAll();
        Optional<Integer> highestRound2=attendaces.stream()
                            .filter(a->a.getUser().getGroup().getGroupId().compareTo(loggedInUser.getGroup().getGroupId())==0)
                            .map(Attendace::getMeetingRound)
                            .max(Integer::compare);
                            int roundValue2=highestRound2.orElse(0);
                            if(roundValue<roundValue2)
                            {
                                socialFundAmount.setIsAttendaceCompleted("1");
                            }
                            else{

                                socialFundAmount.setIsAttendaceCompleted("0");
                            }
        return socialFundAmount;
    }

    @Override
    public Group closeRound() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        Group group=groupRepository.findByGroupId(loggedInUser.getGroup().getGroupId());
        group.setCurrentRound(group.getCurrentRound()+1);
        groupRepository.save(group);
        List<Users> users=userRepository.findByGroupGroupId(group.getGroupId());
        users.stream().forEach(u->{
            u.setHasPayedCurrentRound(false);
            u.setHasPayedCurrentSocialFund(false);
            userRepository.save(u);
        });
        return group;
    }
}
