package vsla.meeting.meeting;

import lombok.RequiredArgsConstructor;
import vsla.meeting.meeting.dto.MeetingDTO;
import vsla.meeting.meeting.dto.MeetingDTO2;
import vsla.utils.CurrentlyLoggedInUser;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;

    @Override
    public Meeting EditMeeting(Long meetingId, Meeting meeting) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Meeting meetingToBeUpdated = meetingRepository.findMeetingByMeetingId(meetingId);
        meetingToBeUpdated.setCurrentRound(meeting.getCurrentRound());
        meetingToBeUpdated.setIsEnabled(meeting.getIsEnabled());
        meetingToBeUpdated.setMeetingInterval(meeting.getMeetingInterval());
        meetingToBeUpdated.setMeetingReason(meeting.getMeetingReason());
        meetingToBeUpdated.setMeetingType(meeting.getMeetingType());
        meetingToBeUpdated.setMeetingTypeId(meeting.getMeetingTypeId());
        meetingToBeUpdated.setMeetingIntervalId(meeting.getMeetingIntervalId());
        meetingToBeUpdated.setIntervalDays(meeting.getIntervalDays());
        meetingToBeUpdated.setIsEnabled(meeting.getIsEnabled());
        meetingToBeUpdated.setNextMeetingDate(meeting.getNextMeetingDate());
        meetingToBeUpdated.setCreatedAt(meetingToBeUpdated.getCreatedAt());
        meetingToBeUpdated.setUpdatedAt(localDateTime);
        return meetingRepository.save(meetingToBeUpdated);
    }

    @Override
    public Meeting CancleMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findMeetingByMeetingId(meetingId);
        meeting.setIsEnabled(false);
        return meetingRepository.save(meeting);
    }

    @Override
    public Meeting createMeeting(Meeting meeting) {
        LocalDateTime localDateTime = LocalDateTime.now();
        meeting.setCreatedAt(localDateTime);
        meeting.setUpdatedAt(localDateTime);
        meeting.setIsEnabled(true);
        return meetingRepository.save(meeting);
    }

    @Override
    public List<MeetingDTO> getAllMeetingsByGroup(Long groupId) {
        List<MeetingDTO> meetingDTOs = new ArrayList<MeetingDTO>();
        List<Meeting> meetings = meetingRepository.findByGroupGroupId(groupId);
        meetings.stream().forEach(m -> {
            MeetingDTO meetingDTO = new MeetingDTO();
            meetingDTO.setMeetingId(m.getMeetingId().toString());
            meetingDTO.setCurrentRound(String.valueOf(m.getCurrentRound()));
            meetingDTO.setIntervalDays(String.valueOf(m.getIntervalDays()));
            meetingDTO.setMeetingIntervalId(m.getMeetingIntervalId().toString());
            meetingDTO.setMeetingTypeId(m.getMeetingTypeId().toString());
            meetingDTO.setMeetingInterval(m.getMeetingInterval());
            meetingDTO.setMeetingReason(m.getMeetingReason());
            meetingDTO.setNextMeetingDate(m.getNextMeetingDate().toString());
            meetingDTO.setMeetingType(m.getMeetingType());
            meetingDTOs.add(meetingDTO);
        });
        return meetingDTOs;
    }

    @Override
    public MeetingDTO getMeetingById(Long meetingId) {
        Meeting meeting = meetingRepository.findMeetingByMeetingId(meetingId);

            MeetingDTO meetingDTO = new MeetingDTO();
            meetingDTO.setMeetingId(meeting.getMeetingId().toString());
            meetingDTO.setCurrentRound(String.valueOf(meeting.getCurrentRound()));
            meetingDTO.setIntervalDays(String.valueOf(meeting.getIntervalDays()));
            meetingDTO.setMeetingIntervalId(meeting.getMeetingIntervalId().toString());
            meetingDTO.setMeetingTypeId(meeting.getMeetingIntervalId().toString());
            meetingDTO.setMeetingInterval(meeting.getMeetingInterval());
            meetingDTO.setMeetingReason(meeting.getMeetingReason());
            meetingDTO.setNextMeetingDate(meeting.getNextMeetingDate().toString());
            meetingDTO.setMeetingType(meeting.getMeetingType());
        return meetingDTO;
    }

    @Override
    public Meeting ContinueMeeting(Long meetingId, int nextRound) {
        Meeting meeting = meetingRepository.findMeetingByMeetingId(meetingId);
        // LocalDateTime updatedDate = currentDate.plus(3, ChronoUnit.DAYS);
        LocalDateTime updatedDate = meeting.getNextMeetingDate().plus(meeting.getIntervalDays(), ChronoUnit.DAYS);
        meeting.setNextMeetingDate(updatedDate);
        meeting.setIsEnabled(true);
        meeting.setCurrentRound(nextRound);
        return meetingRepository.save(meeting);
    }

    @Override
    public List<MeetingDTO> getActiveMeetingsByGroup(Long groupId) {
        List<MeetingDTO> meetingDTOs = new ArrayList<MeetingDTO>();
        List<Meeting> meetings = meetingRepository.findMeetingByGroupGroupIdAndIsEnabled(groupId, true);
        meetings.stream().forEach(m -> {
            MeetingDTO meetingDTO = new MeetingDTO();
            meetingDTO.setMeetingId(m.getMeetingId().toString());
            meetingDTO.setCurrentRound(String.valueOf(m.getCurrentRound()));
            meetingDTO.setIntervalDays(String.valueOf(m.getIntervalDays()));
            meetingDTO.setMeetingIntervalId(m.getMeetingIntervalId().toString());
            meetingDTO.setMeetingTypeId(m.getMeetingIntervalId().toString());
            meetingDTO.setMeetingInterval(m.getMeetingInterval());
            meetingDTO.setMeetingReason(m.getMeetingReason());
            meetingDTO.setNextMeetingDate(m.getNextMeetingDate().toString());
            meetingDTO.setMeetingType(m.getMeetingType());
            meetingDTOs.add(meetingDTO);
        });
        return meetingDTOs;
    }

    @Override
    public List<MeetingDTO> getInActiveMeetingsByGroup(Long groupId) {
        List<MeetingDTO> meetingDTOs = new ArrayList<MeetingDTO>();
        List<Meeting> meetings = meetingRepository.findMeetingByGroupGroupIdAndIsEnabled(groupId, false);
        meetings.stream().forEach(m -> {
            MeetingDTO meetingDTO = new MeetingDTO();
            meetingDTO.setMeetingId(m.getMeetingId().toString());
            meetingDTO.setCurrentRound(String.valueOf(m.getCurrentRound()));
            meetingDTO.setIntervalDays(String.valueOf(m.getIntervalDays()));
            meetingDTO.setMeetingIntervalId(m.getMeetingIntervalId().toString());
            meetingDTO.setMeetingTypeId(m.getMeetingIntervalId().toString());
            meetingDTO.setMeetingInterval(m.getMeetingInterval());
            meetingDTO.setMeetingReason(m.getMeetingReason());
            meetingDTO.setNextMeetingDate(m.getNextMeetingDate().toString());
            meetingDTO.setMeetingType(m.getMeetingType());
            meetingDTOs.add(meetingDTO);
        });
        return meetingDTOs;
    }

    @Override
    public List<MeetingDTO2> getAllMeetings(Long organizationId) {
        List<MeetingDTO2> meetingDTOs = new ArrayList<MeetingDTO2>();
        List<Meeting> meetings = meetingRepository.findAll();
        meetings.stream().forEach(m -> {
            if(organizationId.compareTo(m.getGroup().getOrganization().getOrganizationId())==0)
            {
            MeetingDTO2 meetingDTO = new MeetingDTO2();
            meetingDTO.setMeetingId(m.getMeetingId().toString());
            meetingDTO.setCurrentRound(String.valueOf(m.getCurrentRound()));
            meetingDTO.setIntervalDays(String.valueOf(m.getIntervalDays()));
            meetingDTO.setMeetingInterval(m.getMeetingInterval());
            meetingDTO.setMeetingReason(m.getMeetingReason());
            meetingDTO.setNextMeetingDate(m.getNextMeetingDate().toString());
            meetingDTO.setMeetingType(m.getMeetingType());
            meetingDTO.setGroupId(m.getGroup().getGroupId().toString());
            meetingDTOs.add(meetingDTO);
           }
        });
        return meetingDTOs;
    }

}
