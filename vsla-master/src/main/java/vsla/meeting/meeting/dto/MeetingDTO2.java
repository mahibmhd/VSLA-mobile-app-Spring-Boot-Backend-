package vsla.meeting.meeting.dto;

import lombok.Data;

@Data
/**
 * InnerTransactionPage
 */
public class MeetingDTO2 {
    private String meetingId;
    private String currentRound;
    private String nextMeetingDate;
    private String meetingInterval;
    private String intervalDays;
    private String meetingType;
    private String meetingReason;
    private String groupId;
}
