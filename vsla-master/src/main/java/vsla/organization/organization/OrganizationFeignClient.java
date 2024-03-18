package vsla.organization.organization;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import vsla.organization.organization.dto.OrganazationResponse;
@FeignClient(name = "Organization-API.", url = "${VSLA_ADMIN.BASE_URL}" + "organizations")
public interface OrganizationFeignClient {
    @GetMapping()
    List<OrganazationResponse> getAllOrganizations();
}
