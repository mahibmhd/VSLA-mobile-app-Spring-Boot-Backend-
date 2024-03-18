package vsla.payment.Transaction;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import vsla.Otp.Otp;
import vsla.Otp.dto.OtpException;
import vsla.Otp.dto.OtpResponse;
import vsla.exceptions.customExceptions.BadRequestException;
import vsla.group.Group;
import vsla.group.GroupRepository;
import vsla.group.dto.ContributionDto;
import vsla.loan.Loan;
import vsla.loan.LoanRepository;
import vsla.payment.Transaction.dto.ExpenditureDto;
import vsla.payment.Transaction.dto.InnerTransactionPage;
import vsla.payment.Transaction.dto.ReportDto;
import vsla.payment.Transaction.dto.TransactionPage;
import vsla.payment.paymentType.PaymentType;
import vsla.payment.paymentType.PaymentTypeRepository;
import vsla.userManager.user.UserRepository;
import vsla.userManager.user.Users;
import vsla.utils.CurrentlyLoggedInUser;

@Service
@RequiredArgsConstructor
public class TransactionGroupImpl implements TransactionService {
    private final GroupRepository groupRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    private final LoanRepository loanRepository;
      @Value("${MESSAGE_API.BASE_URL}")
    private String messageApiBaseUrl;

    Double roundPaymentAmount = 0.0;
    Double loanDespersalAmount = 0.0;
    Double loanRepaymnetAmount = 0.0;
    Double socialFund=0.0;
    Double userTotalContribution = 0.0;
    Double userTotalDebt = 0.0;

    double totalSaving =0;
    double totalSocialFund=0;
    double totalLoanDespered=0;
    double totalLoanRepaid=0;
    double totalSocialFundReleased=0;
    double totalPenalityAmount=0;
    double totalExpenditure=0;
    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public TransactionPage getTransactionByGroup(Long groupId) {
        roundPaymentAmount = 0.0;
        loanDespersalAmount = 0.0;
        loanRepaymnetAmount = 0.0;
        socialFund = 0.0;
        Group group = groupRepository.findByGroupId(groupId);
        List<Transaction> transactions = transactionRepository.findTransactionByGroup(group);

        List<InnerTransactionPage> innerTransactionPages = new ArrayList<InnerTransactionPage>();
        transactions.stream().forEach(t -> {

            if (t.getPaymentType().getPaymentTypeId() == 1) {
                roundPaymentAmount += t.getAmount();
            }
            if (t.getPaymentType().getPaymentTypeId() == 2) {
                loanDespersalAmount += t.getAmount();
            }
            if (t.getPaymentType().getPaymentTypeId() == 3) {
                loanRepaymnetAmount += t.getAmount();
            }
            if(t.getPaymentType().getPaymentTypeId() == 4){
                socialFund += t.getAmount();
            }

            InnerTransactionPage innerTransactionPage = new InnerTransactionPage();
            innerTransactionPage.setName(t.getPayer().getFullName());
            innerTransactionPage.setGender(t.getPayer().getGender());
            innerTransactionPage.setDate(t.getCreatedAt().toString());
            innerTransactionPage.setAmount(t.getAmount().toString());
            innerTransactionPage.setStatus(t.getStatus());
            innerTransactionPages.add(innerTransactionPage);
        });
        TransactionPage transactionPage = new TransactionPage();
        transactionPage.setAllTransactions(innerTransactionPages);
        int decimalPlaces = 2;
        DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));

        String formattedRP = decimalFormat.format(roundPaymentAmount);
        Double resultRP = Double.parseDouble(formattedRP);
        transactionPage.setRoundPayment(resultRP.toString());

        String formattedLD = decimalFormat.format(loanDespersalAmount);
        Double resultLD = Double.parseDouble(formattedLD);
        transactionPage.setLoanDespersal(resultLD.toString());

        String formattedLR = decimalFormat.format(loanRepaymnetAmount);
        Double resultLR = Double.parseDouble(formattedLR);
        transactionPage.setLoanRepaymnet(resultLR.toString());


       // Double total = roundPaymentAmount + loanDespersalAmount + loanRepaymnetAmount;
        String formattedSF = decimalFormat.format(totalSocialFund);
        Double resultSF = Double.parseDouble(formattedSF);
        transactionPage.setTotal(resultSF.toString());
        return transactionPage;
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @Transactional
    public List<InnerTransactionPage> getSocialFundTransaction(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId);
        List<Transaction> transactions = transactionRepository.findTransactionByGroup(group);
        List<InnerTransactionPage> innerTransactionPages = new ArrayList<InnerTransactionPage>();
        transactions.stream().forEach(t -> {
            InnerTransactionPage innerTransactionPage = new InnerTransactionPage();
            if (t.getPaymentType().getPaymentTypeId() == 4||t.getPaymentType().getPaymentTypeId()==5) {
                innerTransactionPage.setAmount(t.getAmount().toString());
                innerTransactionPage.setGender(t.getPayer().getGender());
                innerTransactionPage.setDate(t.getCreatedAt().toString());
                innerTransactionPage.setName(t.getPayer().getFullName());
                innerTransactionPage.setStatus(t.getStatus());
                innerTransactionPages.add(innerTransactionPage);
            }

        });

        return innerTransactionPages;
    }

    @SuppressWarnings("null")
    @Override
    public Transaction addTransaction(ContributionDto contributionDto) {
        
        String phoneNumber="";
        String message="";
        totalSocialFund=0;
        totalSocialFundReleased=0;
        Users user= currentlyLoggedInUser.getUser();
       Users payer= userRepository.findUsersByUserId(contributionDto.getPayerId());
        Group group= groupRepository.findByGroupId(contributionDto.getGroupId());
        PaymentType paymentType= paymentTypeRepository.findByPaymentTypeId(contributionDto.getPayementTypeId());
        
        
        if(group==null)
        {
             throw new BadRequestException("non-existing group.");
        }
        if(payer==null&&paymentType.getPaymentTypeId()!=7)
        {
             throw new BadRequestException("non-existing user.");
        }
        if(paymentType==null)
        {
            throw new BadRequestException("non-existing Paymnet .");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(contributionDto.getAmount());
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        transaction.setCreatedAt(currentDateAndTime);
        if(paymentType.getPaymentTypeId()==4)
        {   
            phoneNumber=payer.getUsername();
            message="Dear "+payer.getFullName()+" you have paid "+ contributionDto.getAmount()+" ETB for social fund.";
            payer.setHasPayedCurrentSocialFund(true);
            transaction.setStatus("Recieved");
            transaction.setDescription("social fund round payment");
        }
        if(paymentType.getPaymentTypeId()==3)
        {      
            transaction.setStatus("Repaid");
            transaction.setDescription("loan repayment");
        }
        if(paymentType.getPaymentTypeId()==5)
        {
            List<Transaction> transactions=transactionRepository.findTransactionByGroup(user.getGroup());
            transactions.stream().forEach(t->{
                if(t.getPaymentType().getPaymentTypeId()==4)
                {
                    totalSocialFund+=t.getAmount();
                }
                 if(t.getPaymentType().getPaymentTypeId()==5)
                {
                    totalSocialFundReleased+=t.getAmount();
                }
            });
            Double fundableAmount=totalSocialFund-totalSocialFundReleased;
            if(contributionDto.getAmount().compareTo(fundableAmount)>0)
            {
                throw new BadRequestException("no enough fund.");
            }
        }
        if(paymentType.getPaymentTypeId()==6)
        {
            transaction.setStatus("Recieved");
        }
        if(paymentType.getPaymentTypeId()==1)
        {
            phoneNumber=payer.getUsername();
            message="Dear "+payer.getFullName()+" you have paid "+ contributionDto.getAmount()+" ETB for your saving.";
            payer.setHasPayedCurrentRound(true);
             transaction.setDescription("round payemnet");
              transaction.setStatus("Recieved");
        }
        if(paymentType.getPaymentTypeId()==2||paymentType.getPaymentTypeId()==5)
        {
             transaction.setStatus("Disbursed");
            transaction.setDescription(contributionDto.getDescription());
        }
        if(paymentType.getPaymentTypeId()==7)
        {    
            transaction.setStatus("Deducted");
            transaction.setPayer(group.getGroupAdmin());
            List<Transaction> transactions=transactionRepository.findTransactionByGroup(user.getGroup());
            transactions.stream().forEach(t->{
                if (t.getPaymentType().getPaymentTypeId() == 1) {
                    totalSaving += t.getAmount();
                }
                if (t.getPaymentType().getPaymentTypeId() == 2) {
                    totalLoanDespered += t.getAmount();
                }
                if (t.getPaymentType().getPaymentTypeId() == 3) {
                    totalLoanRepaid += t.getAmount();
                }
                if (t.getPaymentType().getPaymentTypeId() == 4) {
                    totalSocialFund += t.getAmount();
                }
                if (t.getPaymentType().getPaymentTypeId() == 5) {
                    totalSocialFundReleased += t.getAmount();
                }
                if (t.getPaymentType().getPaymentTypeId() == 6) {
                    totalPenalityAmount += t.getAmount();
                }
                if (t.getPaymentType().getPaymentTypeId()==7) {
                    totalExpenditure+=t.getAmount();
                }
            });
            Double netFund=totalSaving - totalLoanDespered + totalLoanRepaid + totalPenalityAmount-totalExpenditure;
            if(contributionDto.getAmount().compareTo(netFund)>0)
            {
                throw new BadRequestException("no enough fund.");
            }
        }
        else
        {
            transaction.setPayer(payer);
        }
       
        transaction.setGroup(group);
        transaction.setPaymentType(paymentType);
        transaction.setDescription(contributionDto.getDescription());
        transaction.setRound(contributionDto.getRound());
        if(paymentType.getPaymentTypeId()!=7)
        {
            userRepository.save(payer);
        }
       
        transactionRepository.save(transaction);
         try {
                String uri = messageApiBaseUrl;
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                // headers.setBearerAuth(res3.getBody().getToken());
                String requestBody = "{\"mobile\":" + "\"" + phoneNumber + "\"," + "\"text\":" + "\""
                        + message + "\"" + "}";
                System.out.println(requestBody);
                HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

                @SuppressWarnings("null")
                ResponseEntity<OtpResponse> res = restTemplate.exchange(uri, HttpMethod.POST, request,
                        OtpResponse.class);

               System.out.println(res);
            } catch (Exception e) {
                OtpException response = new OtpException("error", e.getMessage());
                System.out.println(response);
            }
        return transaction;
    }

    @Override
    public TransactionPage getTransactionByProject(Long projectId) {
       List<Group> groups=groupRepository.findByProjectProjectId(projectId);
       roundPaymentAmount = 0.0;
        loanDespersalAmount = 0.0;
        loanRepaymnetAmount = 0.0;
        List<InnerTransactionPage> innerTransactionPages = new ArrayList<InnerTransactionPage>();
        groups.stream().forEach(g->{
        List<Transaction> transactions = transactionRepository.findTransactionByGroup(g);
         transactions.stream().forEach(t -> {

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
        });
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

    @Override
    public List<ReportDto> getReports() {
        totalSaving=0;
        totalSocialFund=0;
        totalLoanDespered=0;
        totalLoanRepaid=0;
        totalSocialFundReleased=0;
        totalPenalityAmount=0;

        userTotalContribution=0.0;
        userTotalDebt=0.0;
       Users loggedInUser=currentlyLoggedInUser.getUser();
       List<ReportDto> reportDtos= new ArrayList<ReportDto>();
       List<Transaction> transactions2=transactionRepository.findTransactionByGroup(loggedInUser.getGroup());
       transactions2.stream().forEach(t->{
          if(t.getPaymentType().getPaymentTypeId()==1)
          {
              totalSaving+=t.getAmount();
          }
          if(t.getPaymentType().getPaymentTypeId()==2)
          {
              totalLoanDespered+=t.getAmount();
          }
          if(t.getPaymentType().getPaymentTypeId()==3)
          {
              totalLoanRepaid+=t.getAmount();
          }
           if(t.getPaymentType().getPaymentTypeId()==4)
          {
              totalSocialFund+=t.getAmount();
          }
           if(t.getPaymentType().getPaymentTypeId()==5)
          {
              totalSocialFundReleased+=t.getAmount();
          }
          if(t.getPaymentType().getPaymentTypeId()==6)
          {
              totalPenalityAmount+=t.getAmount();
          }
          

      });
       List<Users> users= userRepository.findByGroupGroupId(loggedInUser.getGroup().getGroupId());
       users.stream().forEach(u->{
        userTotalDebt=0.0;
         userTotalContribution=0.0;
        ReportDto reportDto= new ReportDto();
        reportDto.setMemberName(u.getFullName());
        //set total contribution
        List<Transaction> transactions= transactionRepository.findTransactionByPayer(u);
        transactions.stream().forEach(t->{
            if(t.getPaymentType().getPaymentTypeId()==1)
            {
                userTotalContribution+=t.getAmount();
            }
        });
        reportDto.setTotalContribution(userTotalContribution.toString());
        //set total debt
        List<Loan> loans= loanRepository.findByLoanRequester(u);
        loans.stream().forEach(l->{
            if(l.getStatus().equals("active")||l.getStatus().equals("lost"))
            {
                userTotalDebt+=l.getAmountToPay();
            }
        });
        reportDto.setDebt(userTotalDebt.toString());
        //set profit
        
        Double totalLoanFund=totalSaving-totalLoanDespered+totalLoanRepaid+totalPenalityAmount;
        Double totalShare=totalSaving/loggedInUser.getGroup().getShareAmount();
        Double newShareValue=totalLoanFund/totalShare;
        Double membersShareNumber=userTotalContribution/loggedInUser.getGroup().getShareAmount();
        Double memberGrossShare=membersShareNumber*newShareValue;
        Double memberNetShare=memberGrossShare-userTotalDebt;
        int decimalPlaces = 2;

            // Create a DecimalFormat object with the desired pattern
            DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));

            // Format the double value to a string with the specified number of decimal
            // places
            String formatted = decimalFormat.format(memberGrossShare);
            // Parse the formatted string back into a double
             Double result = Double.parseDouble(formatted);
             String formatted2 = decimalFormat.format(memberNetShare);
            // Parse the formatted string back into a double
             Double result2 = Double.parseDouble(formatted2);
         reportDto.setGrossShare(result.toString());
         reportDto.setNetShare(result2.toString());

        reportDtos.add(reportDto);
       });
    reportDtos.sort(Comparator.comparingDouble(reportDto -> Double.parseDouble(((ReportDto) reportDto).getNetShare())).reversed());
    return reportDtos;
    }

    @Override
    public List<ExpenditureDto> getExpenditures() {
        Users user= currentlyLoggedInUser.getUser();
        List<Transaction> transactions=transactionRepository.findTransactionByGroup(user.getGroup());
        List<ExpenditureDto> expenditureDtos= new ArrayList<ExpenditureDto>();
        transactions.forEach(t->{
            if(t.getPaymentType().getPaymentTypeId()==7)
            {
                ExpenditureDto expenditureDto= new ExpenditureDto();
                expenditureDto.setDescription(t.getDescription());
                expenditureDto.setPaidAmount(t.getAmount().toString());
                expenditureDtos.add(expenditureDto);
            }  
        });
        return expenditureDtos;
    }

    
}
