package vsla.Otp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import vsla.Otp.dto.OtpException;
import vsla.Otp.dto.OtpRequest;
import vsla.Otp.dto.OtpResponse;
import vsla.Otp.dto.VerifyRequest;
import vsla.payment.Transaction.dto.SuccessResponse;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }
    @Value("${MESSAGE_API.BASE_URL}")
    private String messageApiBaseUrl;

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<?> sendOtp(OtpRequest otpRequest) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:s");
        Date currentDate = new Date();
        if (this.otpRepository.findOtpByPhoneNumber(otpRequest.getPhoneNumber()) != null) {
            otpRepository.deleteById(otpRepository.findOtpByPhoneNumber(otpRequest.getPhoneNumber()).getOtpId());
        }
        if (this.otpRepository.findOtpByPhoneNumber(otpRequest.getPhoneNumber()) == null) {
            try {
                String uri = messageApiBaseUrl;
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                // headers.setBearerAuth(res3.getBody().getToken());
                String code = getRandomNumberString();
                String requestBody = "{\"mobile\":" + "\"" + otpRequest.getPhoneNumber() + "\"," + "\"text\":" + "\""
                        + "Your OTP code is " + code + "\"" + "}";
                System.out.println(requestBody);
                HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

                ResponseEntity<OtpResponse> res = restTemplate.exchange(uri, HttpMethod.POST, request,
                        OtpResponse.class);

                Calendar c = Calendar.getInstance();
                c.setTime(currentDate);
                c.add(Calendar.MINUTE, 0);
                Date expirayDate = c.getTime();
                System.out.println(dateFormat.format(currentDate));
                System.out.println(dateFormat.format(expirayDate));

                Otp otp = new Otp();
                otp.setPhoneNumber(otpRequest.getPhoneNumber());
                otp.setOtpCode(code);
                otp.setOtpExpireDate(dateFormat.format(expirayDate));
                otpRepository.save(otp);
                return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
            } catch (Exception e) {
                OtpException response = new OtpException("error", e.getMessage());
                return new ResponseEntity<OtpException>(response, HttpStatus.NOT_FOUND);
            }
        } else {
            OtpException response = new OtpException("error", "otp already sent");
            return new ResponseEntity<OtpException>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> VerifyOtp(VerifyRequest verifyRequest, String phoneNumber) {
        Otp otp = otpRepository.findOtpByPhoneNumber(phoneNumber);
        if (otp != null) {
            if (otp.getOtpCode().equals(verifyRequest.getCode())) {
                SuccessResponse successResponse = new SuccessResponse("Success", "Verfied");
                return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.OK);
            } else {
                SuccessResponse successResponse = new SuccessResponse("Error", "Incorrect code");
                return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.BAD_REQUEST);
            }
        } else {
            SuccessResponse successResponse = new SuccessResponse("Error", "Request for code first");
            return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
