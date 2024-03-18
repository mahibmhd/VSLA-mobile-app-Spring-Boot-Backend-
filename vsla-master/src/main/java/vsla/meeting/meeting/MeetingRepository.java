package vsla.meeting.meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByGroupGroupId(Long groupId);
    Meeting findMeetingByMeetingId(Long meetingId);
    List<Meeting> findMeetingByGroupGroupIdAndIsEnabled(Long groupId,Boolean isEnabled);
}