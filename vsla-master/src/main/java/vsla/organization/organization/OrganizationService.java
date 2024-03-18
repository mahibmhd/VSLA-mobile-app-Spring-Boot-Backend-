package vsla.organization.organization;

import java.util.List;

import vsla.payment.Transaction.dto.TransactionPage;

public interface OrganizationService {
    Organization createOrganization(Organization registrationReq);

    Organization updateOrganization(Long organizationId, Organization updateReq);

    List<Organization> getAllOrganizations();

    Organization getOrganizationById(Long organizationId);

    void deleteOrganization(Long organizationId);

    //int getTotalTransactionsForOrganization(Long organizationId);

    TransactionPage getTransactionByOrganization(Long organizationId);
}
