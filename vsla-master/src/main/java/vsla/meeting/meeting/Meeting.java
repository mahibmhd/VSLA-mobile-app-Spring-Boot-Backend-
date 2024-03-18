package vsla.meeting.meeting;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vsla.group.Group;
import java.time.LocalDateTime;

@Entity
@Table(name = "meetings")
@Data
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long meetingId;

    @Column(name = "current_round")
    private int currentRound;

    @Column(name = "meeting_date")
    private LocalDateTime nextMeetingDate;

    @Column(name = "meeting_interval")
    private String meetingInterval;

    @Column(name = "interval_Days")
    private Integer intervalDays;

    @Column(name = "meeting_type")
    private String meetingType;

    @Column(name = "meeting_type_id")
    private Long meetingTypeId;

    @Column(name = "meeting_interval_id")
    private Long meetingIntervalId;

    @Column(name = "meeting_reseaon")
    private String meetingReason;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_enabled")
    private Boolean isEnabled;

}
