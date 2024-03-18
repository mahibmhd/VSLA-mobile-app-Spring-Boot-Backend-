package vsla.organization.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vsla.exceptions.customExceptions.ResourceNotFoundException;
import vsla.organization.organization.Organization;
import vsla.organization.organization.OrganizationService;
import vsla.organization.project.dto.ProjectReq;
import vsla.utils.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final OrganizationService organizationService;

    @Override
    public Project createProject(ProjectReq projectReq) {
        Organization organization = organizationService.getOrganizationById(projectReq.getOrganizationId());

        // Create a new Project
        Project project = new Project();
        project.setProjectName(projectReq.getProjectName());
        project.setStatus(Status.ACTIVE);
        project.setOrganization(organization);

        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long groupTypeId, ProjectReq projectReq) {
        Project project = getProjectById(groupTypeId);

        if (projectReq.getProjectName() != null)
            project.setProjectName(projectReq.getProjectName());

        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjectsByOrganization(Long organizationId) {
        return projectRepository.findByOrganizationOrganizationId(organizationId);
    }

    @Override
    public void deleteProject(Long groupTypeId) {
        getProjectById(groupTypeId);
        projectRepository.deleteById(groupTypeId);
    }

    @Override
    public Project getProjectById(Long groupTypeId) {
        return projectRepository.findById(groupTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + groupTypeId));
    }

    @Override
    public int countGroupsByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            return project.getGroup().getGroupSize();
        }
        return 0;
    }

    @Override
    public List<Project> getProject() {
      return projectRepository.findAll();
    }

   // public int countGroupsByProjectId(Long projectId) {
     //   Project project = projectRepository.findById(projectId).orElse(null);
       // if (project != null) {
         //   return project.getGroups().size();
       // }
        //return 0;
   // }
}
