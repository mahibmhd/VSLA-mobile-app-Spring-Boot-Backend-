package vsla.userManager.user;

import vsla.userManager.user.dto.UserRegistrationReq;
import vsla.userManager.user.dto.UserResponse;
import vsla.userManager.user.dto.UserUpdateReq;

import java.util.List;

public interface UserService {
    UserResponse register(UserRegistrationReq userReq);

    UserResponse me();

    Users getUserByPhoneNumber(String phoneNumber);

    List<UserResponse> getAllUsers();

    List<Users> getUsersByGroup(Long groupId);

    UserResponse editUser(UserUpdateReq updateReq);
}
