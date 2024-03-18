package vsla.payment.Transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class TransactionPage {
    private String roundPayment;
    private String loanDespersal;
    private String loanRepaymnet;
    private String total;
    private List<InnerTransactionPage> allTransactions;
}
