package vsla.Attendance;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vsla.payment.Transaction.dto.SuccessResponse;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/Attendance")
@RequiredArgsConstructor
public class AttendaceController {
    private final AttendaceService attendaceService;

    @PostMapping("/add")
    public SuccessResponse add(@RequestBody List<Attendace> attendaces) {
       attendaceService.addAttendance(attendaces);
        SuccessResponse successResponse=new SuccessResponse("Attendance Recorded Successfully", "Success");
        return successResponse;
    }
    
}
