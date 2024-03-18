package vsla.organization.groupType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupTypeRepository extends JpaRepository<GroupType, Long> {
    List<GroupType> findByOrganizationOrganizationId(Long organizationId);
}