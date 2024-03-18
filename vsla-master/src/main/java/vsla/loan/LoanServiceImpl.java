package vsla.loan;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vsla.exceptions.customExceptions.BadRequestException;
import vsla.loan.dto.LoanAddRequestDto;
import vsla.loan.dto.LoanDataDto;
import vsla.loan.dto.LoanListDto;
import vsla.loan.dto.LoanPageDto;
import vsla.loan.dto.RepaymentAmount;
import vsla.payment.Transaction.Transaction;
import vsla.payment.Transaction.TransactionRepository;
import vsla.payment.Transaction.dto.SuccessResponse;
import vsla.payment.paymentType.PaymentType;
import vsla.payment.paymentType.PaymentTypeRepository;
import vsla.userManager.user.UserRepository;
import vsla.userManager.user.Users;
import vsla.utils.CurrentlyLoggedInUser;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final CurrentlyLoggedInUser loggedInUser;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final TransactionRepository transactionRepository;
    Double pendingAmount = 0.0;
    Double activeAmount = 0.0;
    Double repaidAmount = 0.0;
    Double lostAmount = 0.0;
    double totalSaving =0;
    double totalSocialFund=0;
    double totalLoanDespered=0;
    double totalLoanRepaid=0;
    double totalSocialFundReleased=0;
    double totalPenalityAmount=0;
    

    @Override
    public LoanPageDto getLoanPageData() {
        pendingAmount = 0.0;
        activeAmount = 0.0;
        repaidAmount = 0.0;
        lostAmount = 0.0;
        Users user = loggedInUser.getUser();
        List<Loan> loans = loanRepository.findLoanByGroup(user.getGroup());
        List<LoanListDto> loanListDtos = new ArrayList<LoanListDto>();
        loans.stream().forEach(l -> {
            LoanListDto loanListDto = new LoanListDto();
            loanListDto.setLoanId(l.getLoanId().toString());
            loanListDto.setAmount(l.getAmount().toString());
            if (l.getStatus().equals("pending")) {
                loanListDto.setDueDate("-");
            } else {
                loanListDto.setDueDate(l.getDueDate());

            }
            int decimalPlaces = 2;

            // Create a DecimalFormat object with the desired pattern
            DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));

            // Format the double value to a string with the specified number of decimal
            // places
            Double amountUpdated=l.getAmountToPay()-l.getPaidAmount();
            String formatted = decimalFormat.format(amountUpdated);
            // Parse the formatted string back into a double
            Double result = Double.parseDouble(formatted);
            loanListDto.setAmountToBePaid(result.toString());
            loanListDto.setRequester(l.getLoanRequester().getFullName());
            loanListDto.setInterestRate(l.getInterest().toString());
            loanListDto.setStatus(l.getStatus());
            loanListDto.setGender(l.getLoanRequester().getGender());
            loanListDto.setUpdatedDate(l.getUpdatedAt().toString());
            if (l.getStatus().equals("pending")) {
                pendingAmount += l.getAmount();
            }
            if (l.getStatus().equals("active")) {
                activeAmount += l.getAmount();
            }
            if (l.getStatus().equals("repaid")) {
                repaidAmount += l.getAmount();
            }
            if (l.getStatus().equals("rejected")) {
                lostAmount += l.getAmount();
            }
            loanListDtos.add(loanListDto);
        });
       
        int decimalPlaces = 2;

        // Create a DecimalFormat object with the desired pattern
        DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));
        LoanPageDto loanPageDto = new LoanPageDto();
        String formattedAV = decimalFormat.format(activeAmount);
        Double resultAV = Double.parseDouble(formattedAV);
        loanPageDto.setActiveValue(resultAV.toString());

        String formattedPV = decimalFormat.format(pendingAmount);
        Double resultPV = Double.parseDouble(formattedPV);
        loanPageDto.setPendingValue(resultPV.toString());

        String formattedRV = decimalFormat.format(repaidAmount);
        Double resultRV = Double.parseDouble(formattedRV);
        loanPageDto.setRepaidValue(resultRV.toString());

        String formattedLV = decimalFormat.format(lostAmount);
        Double resultLV = Double.parseDouble(formattedLV);
        loanPageDto.setLostValue(resultLV.toString());
        
       
        Double totalAmount = activeAmount + pendingAmount + repaidAmount + lostAmount;
        String formattedTA = decimalFormat.format(totalAmount);
        Double resultTA = Double.parseDouble(formattedTA);
        loanPageDto.setTotalValue(resultTA.toString());

        Double activePercentage = (activeAmount * 100) / totalAmount;

        // Format the double value to a string with the specified number of decimal
        // places
        String formattedActive = decimalFormat.format(activePercentage);
        // Parse the formatted string back into a double
        Double resultActive = Double.parseDouble(formattedActive);
        loanPageDto.setActivePercent(resultActive.toString());

        Double pendingPercentage = (pendingAmount * 100) / totalAmount;
        String formattedPending = decimalFormat.format(pendingPercentage);
        // Parse the formatted string back into a double
        Double resultPending = Double.parseDouble(formattedPending);
        loanPageDto.setPendingPercent(resultPending.toString());

        Double repaidPercentage = (repaidAmount * 100) / totalAmount;
        String formattedRepaid = decimalFormat.format(repaidPercentage);
        // Parse the formatted string back into a double
        Double resultRepaid = Double.parseDouble(formattedRepaid);
        loanPageDto.setRepaidPercent(resultRepaid.toString());

        Double lostPercentage = (lostAmount * 100) / totalAmount;
        String formattedLost = decimalFormat.format(lostPercentage);
        // Parse the formatted string back into a double
        Double resultLost = Double.parseDouble(formattedLost);
        loanPageDto.setLostPercent(resultLost.toString());

        loanPageDto.setLoanListDtos(loanListDtos);
        return loanPageDto;

    }
    Boolean alreadyApplied=false;

    @Override
    public ResponseEntity<SuccessResponse> addLoan(LoanAddRequestDto tempLoan, Long userId) {
        Users user = loggedInUser.getUser();
         alreadyApplied=false;
        Users requester = userRepository.findUsersByUserId(userId);
        List<Loan> loans=loanRepository.findByLoanRequester(requester);
        loans.stream().forEach(l->{
            if(l.getStatus().equals("active")||l.getStatus().equals("pending"))
            {
                alreadyApplied=true;
            }
        });
        List<Transaction> transactions=transactionRepository.findTransactionByGroup(user.getGroup());
        transactions.stream().forEach(t->{
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
        Double loanableFund=totalSaving-totalLoanDespered+totalLoanRepaid+totalPenalityAmount;
       
        if (alreadyApplied==true) {
            SuccessResponse successResponse= new SuccessResponse("Loan Already Applied", "error");
                return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);
        }
        else if(loanableFund.compareTo(tempLoan.getAmount())<0)
        {
            SuccessResponse successResponse= new SuccessResponse("There is no sufficient loanable fund", "error");
            return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);
        }
        else
        {
            Loan loan = new Loan();
            loan.setGroup(user.getGroup());
            loan.setAmount(tempLoan.getAmount());
            loan.setDescription(tempLoan.getDescription());
            loan.setInterest(user.getGroup().getInterestRate()/100);
            loan.setStatus("pending");
            loan.setDays(tempLoan.getDays());
            loan.setPaidAmount(0.0);
            loan.setLoanRequester(requester);
            LocalDateTime today = LocalDateTime.now();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            loan.setDueDate(dateFormat.format(currentDate));
            loan.setCreatedAt(today);
            loan.setAmountToPay(tempLoan.getAmount() * (user.getGroup().getInterestRate()/100) + tempLoan.getAmount());
            loan.setUpdatedAt(today);
            loanRepository.save(loan);
                     SuccessResponse successResponse= new SuccessResponse("Loan Applied Successfully", "Success");
                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
      
    }

    @Override
    public Loan approveLoan(Long loanId) {
        Transaction transaction= new Transaction();
        Loan loan = loanRepository.findByLoanId(loanId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, loan.getDays());
        Date dueDate = c.getTime();
        loan.setDueDate(dateFormat.format(dueDate));
        LocalDateTime localDateTime = LocalDateTime.now();
        loan.setUpdatedAt(localDateTime);
        loan.setStatus("active");
        transaction.setAmount(loan.getAmount());
        transaction.setCreatedAt(localDateTime);
        transaction.setDescription("loan dispersal");
        transaction.setRound(0);
        transaction.setStatus("Issued");
        transaction.setUpdatedAt(localDateTime);
        transaction.setGroup(loan.getGroup());
        transaction.setPayer(loan.getGroup().getGroupAdmin());
        PaymentType paymentType= paymentTypeRepository.findByPaymentTypeId(2L);
        transaction.setPaymentType(paymentType);
        transactionRepository.save(transaction);
        return loanRepository.save(loan);
    }

    @Override
    public ResponseEntity<?> repayLoan(Long loanId,RepaymentAmount repaymentAmount) {
        repaidAmount=0.0;
        Users user=loggedInUser.getUser();
        Transaction transaction= new Transaction();
        Loan loan = loanRepository.findByLoanId(loanId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, loan.getDays());
        Date dueDate = c.getTime();
        loan.setDueDate(dateFormat.format(dueDate));
        LocalDateTime localDateTime = LocalDateTime.now();
        if(loan.getStatus().equals("pending")||loan.getStatus().equals("lost")||loan.getStatus().equals("repaid")||loan.getStatus().equals("rejected"))
        {
                 throw new BadRequestException("only active loans can be repaid");
        }
        loan.setUpdatedAt(localDateTime);
        // List<Loan> loans=loanRepository.findByLoanRequesterAndStatus(loan.getLoanRequester(),"active");
        // loans.stream().forEach(l->{
        //     repaidAmount+=l.getPaidAmount();
        // });
        Double amountToCompare=loan.getPaidAmount()+repaymentAmount.getAmount();
        if(amountToCompare.compareTo(loan.getAmountToPay())<=0)
        {
            if(amountToCompare.compareTo(loan.getAmountToPay())==0)
            {
                loan.setStatus("repaid");
            }    
        transaction.setAmount(repaymentAmount.getAmount());
        transaction.setCreatedAt(localDateTime);
        transaction.setDescription("loan repayment");
        transaction.setRound(user.getGroup().getCurrentRound());
        transaction.setStatus("Repaid");
        transaction.setUpdatedAt(localDateTime);
        transaction.setGroup(loan.getGroup());
        transaction.setPayer(loan.getLoanRequester());
        PaymentType paymentType= paymentTypeRepository.findByPaymentTypeId(3L);
        transaction.setPaymentType(paymentType);
        transactionRepository.save(transaction);
        Double amountUpdated=loan.getPaidAmount()+repaymentAmount.getAmount();
        loan.setPaidAmount(amountUpdated);
        loanRepository.save(loan);
        SuccessResponse response = new SuccessResponse("loan repaid succesfully","success");
        return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
        }
        if(amountToCompare.compareTo(loan.getAmountToPay())>0)
        {
            throw new BadRequestException("amount exceeds the expected repayment amount");
        }
        else
        {
            throw new BadRequestException("Bad request");
        }
        
    }

    @Override
    public LoanPageDto getLoanPageDataForAdmin(Long organizationId) {
        pendingAmount = 0.0;
        activeAmount = 0.0;
        repaidAmount = 0.0;
        lostAmount = 0.0;
       // Users user = loggedInUser.getUser();
        List<Loan> loans = loanRepository.findAll();
        List<LoanListDto> loanListDtos = new ArrayList<LoanListDto>();
        loans.stream().forEach(l -> {
            if(l.getGroup().getOrganization().getOrganizationId().compareTo(organizationId)==0)
            {
                 LoanListDto loanListDto = new LoanListDto();
            loanListDto.setLoanId(l.getLoanId().toString());
            loanListDto.setAmount(l.getAmount().toString());
            if (l.getStatus().equals("pending")) {
                loanListDto.setDueDate("-");
            } else {
                loanListDto.setDueDate(l.getDueDate());

            }
            int decimalPlaces = 2;

            // Create a DecimalFormat object with the desired pattern
            DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));

            // Format the double value to a string with the specified number of decimal
            // places
            String formatted = decimalFormat.format(l.getAmountToPay());
            // Parse the formatted string back into a double
            Double result = Double.parseDouble(formatted);
            loanListDto.setAmountToBePaid(result.toString());
            loanListDto.setRequester(l.getLoanRequester().getFullName());
            loanListDto.setStatus(l.getStatus());
            loanListDto.setGender(l.getLoanRequester().getGender());
            loanListDto.setUpdatedDate(l.getUpdatedAt().toString());
            if (l.getStatus().equals("pending")) {
                pendingAmount += l.getAmount();
            }
            if (l.getStatus().equals("active")) {
                activeAmount += l.getAmount();
            }
            if (l.getStatus().equals("repaid")) {
                repaidAmount += l.getAmount();
            }
            if (l.getStatus().equals("rejected")) {
                lostAmount = +l.getAmount();
            }
            loanListDtos.add(loanListDto);
            }
        });
        LoanPageDto loanPageDto = new LoanPageDto();
        loanPageDto.setActiveValue(activeAmount.toString());
        loanPageDto.setPendingValue(pendingAmount.toString());
        loanPageDto.setRepaidValue(repaidAmount.toString());
        loanPageDto.setLostValue(lostAmount.toString());
        Double totalAmount = activeAmount + pendingAmount + repaidAmount + lostAmount;
        loanPageDto.setTotalValue(totalAmount.toString());

        Double activePercentage = (activeAmount * 100) / totalAmount;
        int decimalPlaces = 2;

        // Create a DecimalFormat object with the desired pattern
        DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));

        // Format the double value to a string with the specified number of decimal
        // places
        String formattedActive = decimalFormat.format(activePercentage);
        // Parse the formatted string back into a double
        Double resultActive = Double.parseDouble(formattedActive);
        loanPageDto.setActivePercent(resultActive.toString());

        Double pendingPercentage = (pendingAmount * 100) / totalAmount;
        String formattedPending = decimalFormat.format(pendingPercentage);
        // Parse the formatted string back into a double
        Double resultPending = Double.parseDouble(formattedPending);
        loanPageDto.setPendingPercent(resultPending.toString());

        Double repaidPercentage = (repaidAmount * 100) / totalAmount;
        String formattedRepaid = decimalFormat.format(repaidPercentage);
        // Parse the formatted string back into a double
        Double resultRepaid = Double.parseDouble(formattedRepaid);
        loanPageDto.setRepaidPercent(resultRepaid.toString());

        Double lostPercentage = (lostAmount * 100) / totalAmount;
        String formattedLost = decimalFormat.format(lostPercentage);
        // Parse the formatted string back into a double1
        Double resultLost = Double.parseDouble(formattedLost);
        loanPageDto.setLostPercent(resultLost.toString());

        loanPageDto.setLoanListDtos(loanListDtos);
        return loanPageDto;

    }
    int i;
    @Override
    public List<LoanDataDto> getActiveLoans(Long organizationId) {
        List<LoanDataDto>  loanDataDtos= new ArrayList<LoanDataDto>();
        activeAmount = 0.0;
        for(i=1;i<=12;i++)
        {
            List<Loan> loans= loanRepository.findLoansByMonth(i);
            loans.stream().forEach(l->{
                LoanDataDto loanDataDto= new LoanDataDto();
                if(l.getStatus().equals("active")&&l.getLoanRequester().getOrganization().getOrganizationId().compareTo(organizationId)==0)
                {
                    activeAmount+=l.getAmount();
                    loanDataDto.setAmount(activeAmount);
                    String monthName = new DateFormatSymbols().getMonths()[i - 1];
                    loanDataDto.setMonth(monthName);
                    loanDataDto.setStatus("active");
                }
                if(loanDataDto.getAmount()!=null)
                {
                    loanDataDtos.add(loanDataDto);
                }
            });
    
        }
       return loanDataDtos;
    }

    @Override
    public List<LoanDataDto> getInActiveLoans(Long organizationId) {
        List<LoanDataDto>  loanDataDtos= new ArrayList<LoanDataDto>();
        activeAmount = 0.0;
        for(i=1;i<=12;i++)
        {
            List<Loan> loans= loanRepository.findLoansByMonth(i);
            loans.stream().forEach(l->{
                LoanDataDto loanDataDto= new LoanDataDto();
                if(l.getStatus().equals("repaid")&&l.getLoanRequester().getOrganization().getOrganizationId().compareTo(organizationId)==0)
                {
                    activeAmount+=l.getAmount();
                    loanDataDto.setAmount(activeAmount);
                    String monthName = new DateFormatSymbols().getMonths()[i - 1];
                    loanDataDto.setMonth(monthName);
                    loanDataDto.setStatus("repaid");
                }
                if(loanDataDto.getAmount()!=null)
                {
                    loanDataDtos.add(loanDataDto);
                }
                
            });
    
        }
       return loanDataDtos;
    }

    @Override
    public Loan rejectLoan(Long loanId) {
        Loan loan = loanRepository.findByLoanId(loanId);
        LocalDateTime localDateTime = LocalDateTime.now();
        loan.setUpdatedAt(localDateTime);
        loan.setStatus("rejected");
        return loanRepository.save(loan);
    }

   
    
       
    }


   

