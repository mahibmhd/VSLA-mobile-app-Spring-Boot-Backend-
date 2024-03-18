package vsla.organization.project;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "Project-API.", url = "${VSLA_ADMIN.BASE_URL}" + "projects")
public interface ProjectFeignClient {
    @GetMapping("/organization/{organizationId}")
    List<Project> getAllProjectsByOrganization(@PathVariable Long organizationId);
}
