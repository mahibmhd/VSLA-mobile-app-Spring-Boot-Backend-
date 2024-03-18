package vsla.payment.paymentType;

import vsla.payment.paymentType.dto.PaymentTypeReq;

import java.util.List;

public interface PaymentTypeService {
    PaymentType createPaymentType(PaymentTypeReq paymentTypeReq);

    PaymentType updatePaymentType(Long paymentTypeId, PaymentTypeReq paymentTypeReq);

    List<PaymentType> getAllPaymentTypes();

    PaymentType getPaymentTypeById(Long paymentTypeId);
}
