package vsla.Otp;

import org.springframework.http.ResponseEntity;

import vsla.Otp.dto.OtpRequest;
import vsla.Otp.dto.VerifyRequest;

public interface OtpService {
    ResponseEntity<?> sendOtp(OtpRequest otpRequest);
    ResponseEntity<?> VerifyOtp(VerifyRequest verifyRequest,String phoneNumber);
}
