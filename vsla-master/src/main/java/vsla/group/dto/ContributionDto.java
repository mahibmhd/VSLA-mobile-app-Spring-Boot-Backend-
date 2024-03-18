package vsla.group.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContributionDto {
    @NotBlank(message = "group Id is Required")
    private Long groupId;
    @NotBlank(message = "payer Id is Required")
    private Long payerId;
    @NotBlank(message = "payement Type Id is Required")
    private Long payementTypeId;
    @NotBlank(message = "amount is Required")
    private Double amount;
    @NotBlank(message = "round is Required")
    private Integer round;
    @NotBlank(message = "description is Required")
    private String description;
}
