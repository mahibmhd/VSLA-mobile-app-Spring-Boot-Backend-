package vsla.Attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AttendaceRepository  extends JpaRepository<Attendace,Long>{
    List<Attendace> findByMeetingRound(int meetingRound);
}
