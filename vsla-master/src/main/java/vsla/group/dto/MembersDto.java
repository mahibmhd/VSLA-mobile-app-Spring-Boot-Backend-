package vsla.group.dto;

import lombok.Data;

@Data
public class MembersDto {
    private String userId;
    private String fullName;
    private String gender;
    private String proxy;
    private String round;
    private String maxAmount;
    private String hasPaidCurrentRound;
    private String hasPaidCurrentSocialFund;
}
