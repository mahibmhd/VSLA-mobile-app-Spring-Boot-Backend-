package vsla.meeting.MeetingType;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MeetingsTypes", url = "${VSLA_ADMIN.BASE_URL}" + "meeting-types")
public interface MeetingTypeFeignClient {
    @GetMapping("/getAll/App/{organizationId}")
    ResponseEntity<?> getAllMeetingTypes(@PathVariable Long organizationId);
}
