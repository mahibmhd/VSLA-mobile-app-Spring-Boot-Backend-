package vsla.Attendance;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import vsla.userManager.user.Users;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Attendace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attendanceId;
    private boolean isPresent;
    private Date meetingDate;
    private int meetingRound;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

}


