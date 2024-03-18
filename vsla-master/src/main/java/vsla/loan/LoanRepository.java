package vsla.loan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vsla.group.Group;
import vsla.userManager.user.Users;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findLoanByGroup(Group group);
    Loan findByLoanId(Long loanId);
    List<Loan> findByStatus(String status);
    @Query("SELECT l FROM Loan l WHERE MONTH(l.updatedAt) = :month")
    List<Loan> findLoansByMonth(@Param("month") int month);
    List<Loan> findByLoanRequester(Users loanRequester);
    List<Loan> findByLoanRequesterAndStatus(Users loanRequester,String status);
   
}
