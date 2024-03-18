package vsla.userManager.address;


import vsla.userManager.address.dto.AddressRegistrationReq;

public interface AddressService {
    Address addAddress(AddressRegistrationReq registrationReq);
    Address editAddress(Address updatedAddress);
}
