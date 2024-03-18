package vsla.payment.paymentType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vsla.exceptions.customExceptions.ResourceNotFoundException;
import vsla.payment.paymentType.dto.PaymentTypeReq;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    @Override
    public PaymentType createPaymentType(PaymentTypeReq paymentTypeReq) {
        PaymentType meetingType = new PaymentType();
        meetingType.setPaymentTypeName(paymentTypeReq.getPaymentTypeName());

        return paymentTypeRepository.save(meetingType);
    }


    @Override
    public PaymentType updatePaymentType(Long paymentTypeId, PaymentTypeReq paymentTypeReq) {
        PaymentType meetingType = getPaymentTypeById(paymentTypeId);

        if (paymentTypeReq.getPaymentTypeName() != null)
            meetingType.setPaymentTypeName(paymentTypeReq.getPaymentTypeName());

        if (paymentTypeReq.getIsActive() != null)
            meetingType.setActive(paymentTypeReq.getIsActive());

        return paymentTypeRepository.save(meetingType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentType> getAllPaymentTypes() {
        return paymentTypeRepository.findAll();
    }

    @Override
    public PaymentType getPaymentTypeById(Long paymentTypeId) {
        return paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting Type not found"));
    }
}
