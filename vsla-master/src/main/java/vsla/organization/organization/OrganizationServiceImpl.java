package vsla.organization.organization;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vsla.group.Group;
import vsla.organization.project.Project;
import vsla.organization.project.ProjectRepository;
import vsla.payment.Transaction.Transaction;
import vsla.payment.Transaction.TransactionRepository;
import vsla.payment.Transaction.dto.InnerTransactionPage;
import vsla.payment.Transaction.dto.TransactionPage;
import vsla.utils.Status;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;
    private final TransactionRepository transactionRepository;

    Double roundPaymentAmount = 0.0;
    Double loanDespersalAmount = 0.0;
    Double loanRepaymnetAmount = 0.0;

    @Override
    @Transactional
    public Organization createOrganization(Organization registrationReq) {
        // Create a new Project
        Organization organization = new Organization();
        organization.setOrganizationName(registrationReq.getOrganizationName());
        organization.setOrganizationStatus(Status.ACTIVE);

        return organizationRepository.save(organization);
    }

    @Override
    public Organization updateOrganization(Long organizationId, Organization updateReq) {
        Organization organization = getOrganizationById(organizationId);

        if (updateReq.getOrganizationName() != null)
            organization.setOrganizationName(updateReq.getOrganizationName());

        if (updateReq.getOrganizationStatus() != null)
            organization.setOrganizationStatus(updateReq.getOrganizationStatus());

        return organizationRepository.save(organization);
    }

    @Override
    public List<Organization> getAllOrganizations() {

        return organizationRepository.findAll(Sort.by(Sort.Order.asc("organizationId")));
    }

    @Override
    public Organization getOrganizationById(Long organizationId) {
        return organizationRepository.findOrganizationByOrganizationId(organizationId);
    }

    @Override
    public void deleteOrganization(Long organizationId) {
        getOrganizationById(organizationId);
        organizationRepository.deleteById(organizationId);
    }

    @Override
    public TransactionPage getTransactionByOrganization(Long organizationId) {
       roundPaymentAmount = 0.0;
        loanDespersalAmount = 0.0;
        loanRepaymnetAmount = 0.0;
        List<InnerTransactionPage> innerTransactionPages = new ArrayList<InnerTransactionPage>();
            List<Transaction> transactions = transactionRepository.findAll();
            transactions.stream().forEach(t -> {
                if(t.getGroup().getOrganization().getOrganizationId().compareTo(organizationId)==0)
                {
                    if (t.getPaymentType().getPaymentTypeId() == 1) {
                        roundPaymentAmount += t.getAmount();
                    }
                    if (t.getPaymentType().getPaymentTypeId() == 2) {
                        loanDespersalAmount += t.getAmount();
                    }
                    if (t.getPaymentType().getPaymentTypeId() == 3) {
                        loanRepaymnetAmount += t.getAmount();
                    }
                    InnerTransactionPage innerTransactionPage = new InnerTransactionPage();
                    innerTransactionPage.setName(t.getPayer().getFullName());
                    innerTransactionPage.setGender(t.getPayer().getGender());
                    innerTransactionPage.setAmount(t.getAmount().toString());
                    innerTransactionPage.setDate(t.getCreatedAt().toString());
                    innerTransactionPage.setStatus(t.getStatus());
                    innerTransactionPages.add(innerTransactionPage);
                }
            });

        TransactionPage transactionPage = new TransactionPage();
        transactionPage.setAllTransactions(innerTransactionPages);
        transactionPage.setRoundPayment(roundPaymentAmount.toString());
       transactionPage.setLoanDespersal(loanDespersalAmount.toString());
       transactionPage.setLoanRepaymnet(loanRepaymnetAmount.toString());
        Double total = roundPaymentAmount + loanDespersalAmount + loanRepaymnetAmount;
        transactionPage.setTotal(total.toString());
        return transactionPage;
    }

}
