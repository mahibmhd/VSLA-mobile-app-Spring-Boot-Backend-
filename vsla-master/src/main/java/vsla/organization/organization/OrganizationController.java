package vsla.organization.organization;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsla.organization.organization.dto.OrganazationResponse;
import vsla.payment.Transaction.dto.TransactionPage;
import vsla.utils.ApiResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/organizations")
@Tag(name = "Organization API.")
public class OrganizationController {
    private final OrganizationService organizationService;
    private final OrganizationRepository organizationRepository;
    private final OrganizationFeignClient organizationFeignClient;
    @Autowired
    private EntityManager entityManager;
    

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody @Valid Organization registrationReq) {
        Organization createdStore = organizationService.createOrganization(registrationReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<Organization> updateOrganization(
            @PathVariable Long organizationId, @RequestBody @Valid Organization updateReq) {
        return ResponseEntity.ok(organizationService.updateOrganization(organizationId, updateReq));
    }
    @Transactional
    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<OrganazationResponse> organazationResponses=organizationFeignClient.getAllOrganizations();
        List<Organization> updatedOrganizations= new ArrayList<Organization>();
        organazationResponses.stream().forEach(o->{
            Organization organization= new Organization();
            organization.setOrganizationId(o.getOrganizationId());
            organization.setOrganizationName(o.getOrganizationName());
            organization.setOrganizationStatus(o.getOrganizationStatus());
            organization.setUpdatedAt(o.getUpdatedAt());
            organization.setCreatedAt(o.getCreatedAt());
            organization.setOrganizationStatus(o.getOrganizationStatus());
            updatedOrganizations.add(organization);
        });
        //organizationRepository.deleteAll();
        entityManager.createNativeQuery("DELETE FROM organizations WHERE organization_id NOT IN (SELECT organization_id FROM projects)").executeUpdate();
        updatedOrganizations.stream().forEach(uo->{
            organizationRepository.save(uo);
        });
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long organizationId) {
        return ResponseEntity.ok(organizationService.getOrganizationById(organizationId));
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<ApiResponse> deleteOrganization(@PathVariable Long organizationId) {
        organizationService.deleteOrganization(organizationId);
        return ApiResponse.success("Organization deleted successfully");
    }

    @GetMapping("/getTransactionByOrganization/{organizationId}")
    TransactionPage getTransactionByOrganization(@PathVariable Long organizationId) {
        return organizationService.getTransactionByOrganization(organizationId);
    }
}