package vsla.payment.paymentType;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsla.payment.paymentType.dto.PaymentTypeReq;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-types")
@Tag(name = "Payment Type API.")
public class PaymentTypeController {
    private final PaymentTypeService paymentTypeService;

    public PaymentTypeController(PaymentTypeService paymentTypeService) {
        this.paymentTypeService = paymentTypeService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentType>> getAllPaymentTypes() {
        return ResponseEntity.ok(paymentTypeService.getAllPaymentTypes());
    }

    @PostMapping
    public ResponseEntity<PaymentType> createPaymentType(@RequestBody @Valid PaymentTypeReq paymentTypeReq) {
        PaymentType meetingType = paymentTypeService.createPaymentType(paymentTypeReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(meetingType);
    }

    @PutMapping("/{paymentTypeId}")
    public ResponseEntity<PaymentType> updatePaymentType(@PathVariable Long paymentTypeId, @RequestBody PaymentTypeReq paymentTypeReq) {
        return ResponseEntity.ok(paymentTypeService.updatePaymentType(paymentTypeId, paymentTypeReq));
    }
}


