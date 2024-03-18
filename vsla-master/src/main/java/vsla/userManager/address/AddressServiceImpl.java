package vsla.userManager.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vsla.userManager.address.dto.AddressRegistrationReq;
import vsla.utils.CurrentlyLoggedInUser;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final CurrentlyLoggedInUser loggedInUser;

    @Override
    public Address addAddress(AddressRegistrationReq registrationReq) {
        AddressRegistrationReq.validateAddressRegistrationReq(registrationReq);

        Address address = Address.builder()
                .region(registrationReq.getRegion())
                .zone(registrationReq.getZone())
                .woreda(registrationReq.getWoreda())
                .kebele(registrationReq.getKebele())
                .build();

        return addressRepository.save(address);
    }

    @Override
    public Address editAddress(Address updatedAddress) {
        Address address = loggedInUser.getUser().getAddress();

        if (updatedAddress.getRegion() != null)
            address.setRegion(updatedAddress.getRegion());

        if (updatedAddress.getZone() != null)
            address.setZone(updatedAddress.getZone());

        if (updatedAddress.getWoreda() != null)
            address.setWoreda(updatedAddress.getWoreda());

        if (updatedAddress.getKebele() != null)
            address.setKebele(updatedAddress.getKebele());

        return addressRepository.save(address);
    }

}
