package vsla.organization.groupType;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "GroupType-API.", url = "${VSLA_ADMIN.BASE_URL}" + "group-types")
public interface GroupTypeFeignClient {
    @GetMapping("/organization/{organizationId}")
    List<GroupType> getAllGroupTypeByOrganization(@PathVariable Long organizationId);
}
