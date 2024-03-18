package vsla.Otp.dto;

import lombok.Data;

@Data
public class OtpResponse {
    private String message;
    private String timeStamp;
    private String statusCode;    
    private String status;
}
