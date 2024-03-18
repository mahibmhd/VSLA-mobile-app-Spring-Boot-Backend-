package vsla.payment.Transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
 public class SuccessResponse {
    private String message;
    private String status;
 }
