package vsla.organization.project;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vsla.group.Group;
import vsla.group.GroupService;
import vsla.organization.organization.Organization;
import vsla.organization.organization.OrganizationRepository;
import vsla.organization.project.dto.ProjectReportData;
import vsla.organization.project.dto.ProjectReq;
import vsla.userManager.user.Users;
import vsla.utils.ApiResponse;
import vsla.utils.CurrentlyLoggedInUser;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Project API.")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectFeignClient projectFeignClient;
    private final ProjectRepository projectRepository;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    private final GroupService groupService;
    private final OrganizationRepository organizationRepository;

    private int no_of_Total_Groups;

    @Autowired
    private EntityManager entityManager;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody @Valid ProjectReq registrationReq) {
        Project createdStore = projectService.createProject(registrationReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long projectId, @RequestBody @Valid ProjectReq updateReq) {
        return ResponseEntity.ok(projectService.updateProject(projectId, updateReq));
    }

    @Transactional
    @GetMapping("/by-organization")
    public ResponseEntity<List<Project>> getAllProjects() {
        Users loggedInUser = currentlyLoggedInUser.getUser();
        List<Project> projects = projectFeignClient
                .getAllProjectsByOrganization(loggedInUser.getOrganization().getOrganizationId());
        List<Project> updatedProjects = new ArrayList<Project>();
        projects.stream().forEach(p -> {
            Project project = new Project();
            project.setProjectId(p.getProjectId());
            project.setProjectName(p.getProjectName());
            project.setCreatedAt(p.getCreatedAt());
            project.setOrganization(loggedInUser.getOrganization());
            project.setDeleted(false);
            project.setStatus(p.getStatus());
            project.setUpdatedAt(p.getUpdatedAt());
            updatedProjects.add(project);
        });
        entityManager.createNativeQuery("DELETE FROM projects WHERE project_id NOT IN (SELECT project_id FROM groups)")
                .executeUpdate();
        updatedProjects.stream().forEach(up -> {
            projectRepository.save(up);
        });
        return ResponseEntity
                .ok(projectService.getAllProjectsByOrganization(loggedInUser.getOrganization().getOrganizationId()));
    }

    @DeleteMapping({ "/{projectId}" })
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ApiResponse.success("Project deleted successfully");
    }

    @GetMapping("/{projectId}/group-count")
    public ResponseEntity<Integer> countGroupsByProjectId(@PathVariable("projectId") Long projectId) {
        int groupCount = projectService.countGroupsByProjectId(projectId);
        return ResponseEntity.ok(groupCount);
    }

    @GetMapping("/getprojectsReport/{organizationId}")
    List<ProjectReportData> getGrouProjectReportData(@PathVariable Long organizationId) {
        // Users loggedInUser = currentlyLoggedInUser.getUser();
        // Organization organization = loggedInUser.getOrganization();

        List<ProjectReportData> groupReportDatas = new ArrayList<ProjectReportData>();
        List<Project> projects = projectFeignClient.getAllProjectsByOrganization(organizationId);
        projects.stream().forEach(pr -> {
            ProjectReportData projectReportData = new ProjectReportData();
            projectReportData.setProjectName(pr.getProjectName());
            // projectReportData.setNo_of_Groups(pr.getNo_of_groups());

            List<Group> totalGroups = groupService.getGroupsByProject(pr);
            no_of_Total_Groups = 0;
            if(totalGroups!=null)
            {
                totalGroups.stream().forEach(b -> {
                    if (b.getIsActive() == true) {
                        no_of_Total_Groups++;
                    }
                });
            }
           
            projectReportData.setNo_of_Total_Groups(no_of_Total_Groups);
            groupReportDatas.add(projectReportData);

        });

        return groupReportDatas;
    }

}