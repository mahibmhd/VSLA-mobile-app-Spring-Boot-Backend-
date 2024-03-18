package vsla.meeting.meeting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vsla.meeting.meeting.dto.MeetingDTO;
import vsla.meeting.meeting.dto.MeetingDTO2;
import vsla.payment.Transaction.dto.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/meetings")
public class MeetingController {
    @Autowired
    private final MeetingService meetingService;
    @GetMapping("/getAllMeetings/Admin/{organizationId}")
    List<MeetingDTO2> getAllMeetingsForAdmin(@PathVariable Long organizationId) {
        return meetingService.getAllMeetings(organizationId);
    }
    @GetMapping("/getAllMeetings/{groupId}")
    List<MeetingDTO> getAllMeetings(@PathVariable Long groupId) {
        return meetingService.getAllMeetingsByGroup(groupId);
    }

    @GetMapping("/getActiveMeetings/{groupId}")
    List<MeetingDTO> getActiveMeetings(@PathVariable Long groupId) {
        return meetingService.getActiveMeetingsByGroup(groupId);
    }

     @GetMapping("/getInActiveMeetings/{groupId}")
    List<MeetingDTO> getInActiveMeetings(@PathVariable Long groupId) {
        return meetingService.getInActiveMeetingsByGroup(groupId);
    }

    @GetMapping("/getMeetingById/{meetingId}")
    MeetingDTO getMeetingById(@PathVariable Long meetingId) {
        return meetingService.getMeetingById(meetingId);
    }

    @PostMapping("/createMeeting")
    Meeting createMeating(@RequestBody Meeting meeting) {
        return meetingService.createMeeting(meeting);
    }

    @PutMapping("/editMeeting/{meetingId}")
    public ResponseEntity<SuccessResponse> editMeeeting(@PathVariable Long meetingId, @RequestBody	Meeting meeting) {
        
        meetingService.EditMeeting(meetingId, meeting);
         SuccessResponse response = new SuccessResponse("meeting edited successfully","success");
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/cancelMeeting/{meetingId}")
   public ResponseEntity<SuccessResponse> cancelMeeeting(@PathVariable Long meetingId) {
        meetingService.CancleMeeting(meetingId);
        SuccessResponse response = new SuccessResponse("meeting canceld successfully","success");
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/continueMeeting/{meetingId}/{nextRound}")
    public ResponseEntity<SuccessResponse> continueMeeting(@PathVariable Long meetingId,@PathVariable Integer nextRound) {
        meetingService.ContinueMeeting(meetingId, nextRound);
         SuccessResponse response = new SuccessResponse("meeting continued successfully","success");
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
    }



}


