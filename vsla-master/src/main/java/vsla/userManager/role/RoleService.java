package vsla.userManager.role;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vsla.exceptions.customExceptions.ResourceNotFoundException;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Retrieves all roles.
    public List<Role> getRoles() {
        List<Role> roles = roleRepository.findAll(Sort.by(Sort.Direction.ASC, "roleId"));
        if (roles.isEmpty())
            throw new ResourceNotFoundException("No roles found.");

        return roles;
    }

    // Retrieves a role by id.
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found."));
    }

    public Role getRoleByRoleName(String roleName) {
        return roleRepository.findByRoleNameIgnoreCase(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found."));
    }

}
