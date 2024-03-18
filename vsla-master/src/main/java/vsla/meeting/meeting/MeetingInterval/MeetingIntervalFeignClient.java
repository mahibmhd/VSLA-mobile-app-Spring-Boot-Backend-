package vsla.meeting.meeting.MeetingInterval;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MeetingsIntervals", url = "${VSLA_ADMIN.BASE_URL}" + "meeting-intervals")
public interface MeetingIntervalFeignClient {
    @GetMapping("/getAll/App/{organizationId}")
    ResponseEntity<?> getAllMeetingIntervals(@PathVariable Long organizationId);
}
