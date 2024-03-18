package vsla.loan;


import java.util.List;

import org.springframework.http.ResponseEntity;

import vsla.loan.dto.LoanAddRequestDto;
import vsla.loan.dto.LoanDataDto;
import vsla.loan.dto.LoanPageDto;
import vsla.loan.dto.RepaymentAmount;
import vsla.payment.Transaction.dto.SuccessResponse;


public interface LoanService {
    LoanPageDto getLoanPageData();
    List<LoanDataDto> getActiveLoans(Long organizationId);
    List<LoanDataDto> getInActiveLoans(Long organizationId);
    LoanPageDto getLoanPageDataForAdmin(Long organizationId);
    ResponseEntity<SuccessResponse> addLoan(LoanAddRequestDto tempLoan, Long userId);
    Loan approveLoan(Long loanId);
    Loan rejectLoan(Long loanId);
    ResponseEntity<?> repayLoan(Long loanId,RepaymentAmount repaymentAmount);
    
}
