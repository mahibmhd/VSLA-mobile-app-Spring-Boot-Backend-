package vsla.TermsAndConditions;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

 @FeignClient(name = "TermsandConditions", url = "${VSLA_ADMIN.BASE_URL}" + "TermsandConditions")
public interface TermsandConditionsFeignClient {
      @GetMapping("/getTermsandConditions/App/{organizationId}")
    ResponseEntity<?> getAllTermsAndConditions(@PathVariable Long organizationId);
}
