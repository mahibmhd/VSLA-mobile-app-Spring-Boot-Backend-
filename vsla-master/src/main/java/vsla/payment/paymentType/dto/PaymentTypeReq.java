package vsla.payment.paymentType.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentTypeReq {

    @NotBlank(message = "Payment Type Name Name is required")
    private String paymentTypeName;

    private Boolean isActive;
}