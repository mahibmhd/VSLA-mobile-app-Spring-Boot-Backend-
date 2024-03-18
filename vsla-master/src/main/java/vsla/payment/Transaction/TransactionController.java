package vsla.payment.Transaction;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import vsla.group.dto.ContributionDto;
import vsla.payment.Transaction.dto.ExpenditureDto;
import vsla.payment.Transaction.dto.InnerTransactionPage;
import vsla.payment.Transaction.dto.ReportDto;
import vsla.payment.Transaction.dto.SuccessResponse;
import vsla.payment.Transaction.dto.TransactionPage;

@RestController
@RequestMapping("/api/v1/Transactions")
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    private final TransactionService transactionService;

    @GetMapping("/getAllTransactions/{groupId}")
    TransactionPage getTransactions(@PathVariable Long groupId) {
        return transactionService.getTransactionByGroup(groupId);
    }
    @GetMapping("/getExpenditures")
    List<ExpenditureDto> getExpenditures() {
        return transactionService.getExpenditures();
    }
    @GetMapping("/getAllTransactions/report")
    List<ReportDto> getTransactions() {
        return transactionService.getReports();
    }
    @GetMapping("/getAllTransactionsByProject/{projectId}")
    TransactionPage getTransactionsByProjectId(@PathVariable Long projectId) {
        return transactionService.getTransactionByProject(projectId);
    }
    @GetMapping("/getAllTransactions/socialFund/{groupId}")
    List<InnerTransactionPage> getTransactionssocialFund(@PathVariable Long groupId) {
        return transactionService.getSocialFundTransaction(groupId);
    }
    @PostMapping("/addTransaction")
    public ResponseEntity<SuccessResponse> getTransactions(@RequestBody ContributionDto contributionDto) {
        transactionService.addTransaction(contributionDto);
        SuccessResponse response = new SuccessResponse("transaction added succesfully","success");
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
    }
}
