package vsla.Tips;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

 @FeignClient(name = "Tips", url = "${VSLA_ADMIN.BASE_URL}" + "Tips")
public interface TipsFeignClient {

    @GetMapping("/getTips/App/{organizationId}")
    ResponseEntity<?> getAllTips(@PathVariable Long organizationId);
   


    
} 
