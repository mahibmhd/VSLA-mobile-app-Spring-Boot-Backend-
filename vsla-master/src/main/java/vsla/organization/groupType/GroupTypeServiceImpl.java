package vsla.organization.groupType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vsla.exceptions.customExceptions.ResourceNotFoundException;
import vsla.organization.groupType.dto.GroupTypeReq;
import vsla.organization.organization.Organization;
import vsla.organization.organization.OrganizationService;
import vsla.utils.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupTypeServiceImpl implements GroupTypeService {
    private final GroupTypeRepository groupTypeRepository;
    private final OrganizationService organizationService;

    @Override
    public GroupType createGroupType(GroupTypeReq groupTypeReq) {
        Organization organization = organizationService.getOrganizationById(groupTypeReq.getOrganizationId());

        // Create a new Project
        GroupType groupType = new GroupType();
        groupType.setGroupTypeName(groupTypeReq.getGroupTypeName());
        groupType.setStatus(Status.ACTIVE);
        groupType.setOrganization(organization);

        return groupTypeRepository.save(groupType);
    }

    @Override
    public GroupType updateGroupType(Long groupTypeId, GroupTypeReq groupTypeReq) {
        GroupType groupType = getGroupTypeById(groupTypeId);

        if (groupTypeReq.getGroupTypeName() != null)
            groupType.setGroupTypeName(groupTypeReq.getGroupTypeName());

        return groupTypeRepository.save(groupType);
    }

    @Override
    public List<GroupType> getAllGroupTypesByOrganization(Long organizationId) {
        return groupTypeRepository.findByOrganizationOrganizationId(organizationId);
    }

    @Override
    public void deleteGroupType(Long groupTypeId) {
        getGroupTypeById(groupTypeId);
        groupTypeRepository.deleteById(groupTypeId);
    }

    @Override
    public GroupType getGroupTypeById(Long groupTypeId) {
        return groupTypeRepository.findById(groupTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + groupTypeId));
    }

}
