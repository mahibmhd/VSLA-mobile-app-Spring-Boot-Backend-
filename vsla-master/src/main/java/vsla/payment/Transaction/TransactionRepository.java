package vsla.payment.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import vsla.group.Group;
import vsla.userManager.user.Users;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findTransactionByGroup(Group group);
    List<Transaction> findTransactionByPayer(Users payer);
    List<Transaction> findTransactionByPaymentTypePaymentTypeId(Long paymentTypeId);

}
