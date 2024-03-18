package vsla.payment.Transaction.dto;

import lombok.Data;

@Data
/**
 * InnerTransactionPage
 */
public class ReportDto {
    private String memberName;
    private String grossShare;
    private String netShare;
    private String debt;
    private String totalContribution;
}
