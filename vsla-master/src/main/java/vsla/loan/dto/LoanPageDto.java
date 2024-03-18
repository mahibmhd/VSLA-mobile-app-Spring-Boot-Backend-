package vsla.loan.dto;


import java.util.List;

import lombok.Data;

@Data
public class LoanPageDto {
    private String pendingPercent;
    private String pendingValue;
    private String activePercent;
    private String activeValue;
    private String repaidPercent;
    private String repaidValue;
    private String lostPercent;
    private String lostValue;
    private String totalValue;
    private List<LoanListDto> LoanListDtos;
}
