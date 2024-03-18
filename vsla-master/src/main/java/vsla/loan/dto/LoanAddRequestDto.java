package vsla.loan.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanAddRequestDto {
    @NotBlank(message = "Amount is required")
    private Double amount;

    @NotBlank(message = "days is required")
    private int days;

    @NotNull(message = "Description is required")
    private String description;


}
