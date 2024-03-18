package vsla.Attendance;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vsla.userManager.user.UserRepository;
import vsla.userManager.user.Users;

@Service
@RequiredArgsConstructor
public class AttendaceServiceImpl implements AttendaceService{
    private final AttendaceRepository attendaceRepository;
    private final UserRepository userRepository;
    @SuppressWarnings("null")
    @Override
    public List<Attendace> addAttendance(List<Attendace> attendaces) {
        attendaces.stream().forEach(a->{
            Users user=userRepository.findUsersByUserId(a.getUser().getUserId());
            a.setUser(user);
        });
       return attendaceRepository.saveAll(attendaces);
    }
    
}
