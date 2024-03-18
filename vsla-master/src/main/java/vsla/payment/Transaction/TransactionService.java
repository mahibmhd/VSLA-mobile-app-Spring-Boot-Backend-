package vsla.payment.Transaction;



import java.util.List;

import vsla.group.dto.ContributionDto;
import vsla.payment.Transaction.dto.ExpenditureDto;
import vsla.payment.Transaction.dto.InnerTransactionPage;
import vsla.payment.Transaction.dto.ReportDto;
import vsla.payment.Transaction.dto.TransactionPage;

public interface TransactionService {
    TransactionPage getTransactionByGroup(Long groupId);
    Transaction addTransaction(ContributionDto contributionDto);
    List<InnerTransactionPage> getSocialFundTransaction(Long groupId);
    TransactionPage getTransactionByProject(Long projectId);
    List<ReportDto> getReports();
    List<ExpenditureDto> getExpenditures();
}
