package vsla.organization.project;

import vsla.organization.project.dto.ProjectReq;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectReq projectReq);

    Project updateProject(Long groupTypeId, ProjectReq projectReq);

    Project getProjectById(Long groupTypeId);

    List<Project> getAllProjectsByOrganization(Long organizationId);

    void deleteProject(Long groupTypeId);

    int countGroupsByProjectId(Long projectId);

    List<Project> getProject();
   

}



