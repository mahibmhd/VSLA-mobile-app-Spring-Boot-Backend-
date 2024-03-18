package vsla.LoanReason;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LoanReason-API.", url = "${VSLA_ADMIN.BASE_URL}" + "loanReason")
public interface LoanReasonFeignClient {
    @GetMapping("/getloanReasons/App/{organizationId}")
    ResponseEntity<?> getLoanReasonsByOrg(@PathVariable Long organizationId);
}
