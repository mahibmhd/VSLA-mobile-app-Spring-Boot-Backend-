package vsla.Otp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vsla.Otp.dto.OtpRequest;
import vsla.Otp.dto.VerifyRequest;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<?> Send(@RequestBody OtpRequest otpRequest) {
        return otpService.sendOtp(otpRequest);
    }

    @PostMapping("/verify/{phoneNumber}")
    public ResponseEntity<?> Verify(@RequestBody VerifyRequest request,
            @PathVariable String phoneNumber) {
        return otpService.VerifyOtp(request, phoneNumber);
    }

}
