package vsla.userManager.user;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vsla.exceptions.customExceptions.ResourceAlreadyExistsException;
import vsla.exceptions.customExceptions.ResourceNotFoundException;
import vsla.organization.organization.Organization;
import vsla.organization.organization.OrganizationService;
import vsla.userManager.role.Role;
import vsla.userManager.role.RoleService;
import vsla.userManager.user.dto.UserMapper;
import vsla.userManager.user.dto.UserRegistrationReq;
import vsla.userManager.user.dto.UserResponse;
import vsla.userManager.user.dto.UserUpdateReq;
import vsla.utils.CurrentlyLoggedInUser;

import java.util.List;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    private final OrganizationService organizationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public UserResponse register(UserRegistrationReq userReq) {
        if (userRepository.findByUsername(userReq.getPhoneNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Phone number is already taken");

        Users user = createUser(userReq);
        user.setProxyEnabled(true);
        user = userRepository.save(user);
        return UserMapper.toUserResponse(user);
    }

    //    @Cacheable(key = "#productId")
    private Users createUser(UserRegistrationReq userReq) {
        Role role = roleService.getRoleByRoleName(userReq.getRoleName());
        Organization organization = organizationService.getOrganizationById(userReq.getOrganizationId());

        return Users.builder()
                .username(userReq.getPhoneNumber())
                .password(passwordEncoder.encode(userReq.getPassword()))
                .fullName(userReq.getFullName())
                .hasPayedCurrentRound(false)
                .hasPayedCurrentSocialFund(false)
                .deleted(false)
                .proxyEnabled(userReq.getProxyEnabled())
                .gender(userReq.getGender())
                .role(role)
                .organization(organization)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public UserResponse editUser(UserUpdateReq updateReq) {
        Users user = currentlyLoggedInUser.getUser();

        if (updateReq.getFullName() != null)
            user.setFullName(updateReq.getFullName());

        // use phone number as username
        // Update phone number if provided and different from the current phone number
        if (updateReq.getPhoneNumber() != null && !user.getUsername().equals(updateReq.getPhoneNumber())) {
            // Check if the new Phone Number is already taken
            if (userRepository.findByUsername(updateReq.getPhoneNumber()).isPresent())
                throw new ResourceAlreadyExistsException("Phone number is already taken");

            user.setUsername(updateReq.getPhoneNumber());
        }

        user = userRepository.save(user);
        return UserMapper.toUserResponse(user);
    }

    @Override
    public UserResponse me() {
        Users user = currentlyLoggedInUser.getUser();
        return UserMapper.toUserResponse(user);
    }

    //phone number used as username
    @Override
    public Users getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByUsername(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Cacheable
    public List<UserResponse> getAllUsers() {
        List<Users> users = userRepository.findAll(Sort.by(Sort.Order.asc("userId")));
        return users.stream().
                map(UserMapper::toUserResponse).
                toList();
    }

    @Override
    public List<Users> getUsersByGroup(Long groupId) {
        return userRepository.findByGroupGroupId(groupId);
    }
}
