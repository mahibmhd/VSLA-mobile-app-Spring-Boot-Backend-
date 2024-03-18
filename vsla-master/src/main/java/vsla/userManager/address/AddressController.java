package vsla.userManager.address;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsla.userManager.address.dto.AddressRegistrationReq;

@RestController
@RequestMapping("/api/v1/addresses")
@Tag(name = "User Group API.")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody @Valid AddressRegistrationReq registrationReq) {
        Address address = addressService.addAddress(registrationReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }

    @PutMapping
    public ResponseEntity<Address> editAddress(@RequestBody Address address) {
        return ResponseEntity.ok(addressService.editAddress(address));
    }

}


