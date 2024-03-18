package vsla.awareness;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "Awareness-API.", url = "${VSLA_ADMIN.BASE_URL}" + "awareness")
public interface AwarnessFeignClient {
    @GetMapping("/by-group/{groupId}")
    ResponseEntity<?> getAllAwarenessByGroupId(@PathVariable Long groupId);
}
