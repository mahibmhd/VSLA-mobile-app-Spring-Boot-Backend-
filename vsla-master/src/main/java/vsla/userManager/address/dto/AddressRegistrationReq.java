package vsla.userManager.address.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vsla.exceptions.customExceptions.BadRequestException;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AddressRegistrationReq {

    @NotBlank(message = "Region is required")
    private String region;

    @NotBlank(message = "Zone is required")
    private String zone;

    @NotBlank(message = "Woreda is required")
    private String woreda;

    @NotBlank(message = "Kebele is required")
    private String kebele;

    public static void validateAddressRegistrationReq(AddressRegistrationReq request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<AddressRegistrationReq>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String missingFields = violations.stream()
                    .map(violation -> violation.getPropertyPath().toString())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException("Address Required fields: " + missingFields + ".");
        }
    }
}