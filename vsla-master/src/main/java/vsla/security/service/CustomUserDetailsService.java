package vsla.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vsla.exceptions.customExceptions.ResourceNotFoundException;
import vsla.userManager.user.UserRepository;
import vsla.userManager.user.Users;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("There is no user with this username")
        );

//        if (user.getUserStatus().equals(UserStatus.BANNED))
//            throw new BannedUserException("Your account has been temporarily banned. Please contact admins for further assistance.");

        Collection<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();

        return user;
    }

    public void updateLastLogin(String username) {
        Optional<Users> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Users adminUser = user.get();
            adminUser.setLastLoggedIn(LocalDateTime.now());
            userRepository.save(adminUser);
        }
    }

    public Users getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
    }
}