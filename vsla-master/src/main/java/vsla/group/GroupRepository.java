package vsla.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vsla.organization.project.Project;

import java.util.List;


@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByGroupId(Long groupId);

    List<Group> findByOrganizationOrganizationId(Long organizationId);

    List<Group> findByProjectProjectId(Long projectId);
    
    List<Group> findGroupByProject(Project project);
}