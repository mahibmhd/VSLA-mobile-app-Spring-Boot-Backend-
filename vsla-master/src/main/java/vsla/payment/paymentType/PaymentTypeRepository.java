package vsla.payment.paymentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {
    PaymentType findByPaymentTypeId(Long paymentTypeId);
}