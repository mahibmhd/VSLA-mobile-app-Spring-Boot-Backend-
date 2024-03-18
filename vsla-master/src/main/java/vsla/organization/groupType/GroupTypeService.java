package vsla.organization.groupType;


import vsla.organization.groupType.dto.GroupTypeReq;

import java.util.List;

public interface GroupTypeService {
    GroupType createGroupType(GroupTypeReq groupTypeReq);

    GroupType updateGroupType(Long groupTypeId, GroupTypeReq groupTypeReq);

    List<GroupType> getAllGroupTypesByOrganization(Long organizationId);

    void deleteGroupType(Long groupTypeId);

    GroupType getGroupTypeById(Long groupTypeId);
}
