package vsla.group;

import java.time.Month;
import java.util.List;
import java.util.Map;

public class GroupData {

    private List<Group> groups;
    private Map<Month, Integer> groupsPerMonth;

    public GroupData(List<Group> groups, Map<Month, Integer> groupsPerMonth) {
        this.groups = groups;
        this.groupsPerMonth = groupsPerMonth;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Map<Month, Integer> getGroupsPerMonth() {
        return groupsPerMonth;
    }
}

