package vsla.Otp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp,Long>{
    Otp findOtpByPhoneNumber(String phoneNumber); 
}
