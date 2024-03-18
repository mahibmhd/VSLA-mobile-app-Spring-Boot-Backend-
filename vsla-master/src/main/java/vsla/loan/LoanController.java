package vsla.loan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vsla.loan.dto.LoanAddRequestDto;
import vsla.loan.dto.LoanDataDto;
import vsla.loan.dto.LoanPageDto;
import vsla.loan.dto.RepaymentAmount;
import vsla.payment.Transaction.dto.SuccessResponse;
import vsla.utils.ApiResponse;

@RestController
@RequestMapping("/api/v1/Loan")
@RequiredArgsConstructor
public class LoanController {
    @Autowired
    private final LoanService loanService;

    @GetMapping("/LoanPage")
    LoanPageDto getLoanPageData() {
        
        return loanService.getLoanPageData();
    }
     @GetMapping("/AdminLoanPage/{organizationId}")
    LoanPageDto getLoanPageDataForAdmin(@PathVariable Long organizationId) {
        
        return loanService.getLoanPageDataForAdmin(organizationId);
    }
    
    @GetMapping("/AdminLoanData/{organizationId}")
    List<LoanDataDto> getLoanData(@PathVariable Long organizationId) {
        List<LoanDataDto> mergedList = new ArrayList<LoanDataDto>(loanService.getActiveLoans(organizationId)); // Copy the content of list1
        mergedList.addAll(loanService.getInActiveLoans(organizationId));
        return mergedList;
    }


    @PostMapping("/Add/{userId}")
     public ResponseEntity<SuccessResponse>addLoan(@RequestBody LoanAddRequestDto loan,@PathVariable Long userId) {
     return loanService.addLoan(loan,userId);
    }
    @PutMapping("/edit/{loanId}")
     public ResponseEntity<SuccessResponse>editLoan(@PathVariable Long loanId) {
      loanService.approveLoan(loanId);
      SuccessResponse response = new SuccessResponse("loan approved succesfully","success");
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
    }
    @PutMapping("/reject/{loanId}")
     public ResponseEntity<SuccessResponse>rejectLoan(@PathVariable Long loanId) {
      loanService.rejectLoan(loanId);
      SuccessResponse response = new SuccessResponse("loan rejected succesfully","success");
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
    }
     @PutMapping("/edit/repay/{loanId}")
     public ResponseEntity<?> repay(@PathVariable Long loanId, @RequestBody RepaymentAmount repaymentAmount) {
     return loanService.repayLoan(loanId,repaymentAmount);
      
    }

}
