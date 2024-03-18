package vsla.homePage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vsla.payment.Transaction.Transaction;
import vsla.payment.Transaction.TransactionRepository;
import vsla.userManager.user.Users;
import vsla.utils.CurrentlyLoggedInUser;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HomepageService {
    private final CurrentlyLoggedInUser loggedInUser;
    private final TransactionRepository transactionRepository;
    double totalSaving = 0;
    double totalSocialFund = 0;
    double totalLoanDespered = 0;
    double totalLoanRepaid = 0;
    double totalSocialFundReleased = 0;
    double totalPenalityAmount = 0;
    double totalExpenditure=0;

    public HomepageResponse getHomepageResponse() {
        totalSaving = 0;
        totalSocialFund = 0;
        totalLoanDespered = 0;
        totalLoanRepaid = 0;
        totalSocialFundReleased = 0;
        totalPenalityAmount = 0;
        totalExpenditure =0;
        Users user = loggedInUser.getUser();

        // List<RecentContribution> recentContributions = createRandomContributions();
        List<RecentContribution> recentContributions = new ArrayList<RecentContribution>();

        HomepageResponse homepageResponse = new HomepageResponse();
        homepageResponse.setGroupName(user.getGroup().getGroupName());
        List<Transaction> transactions = transactionRepository.findTransactionByGroup(user.getGroup());
        transactions.stream().forEach(t -> {
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
        LocalDate today = LocalDate.now();
        int decimalPlaces = 2;

        // Create a DecimalFormat object with the desired pattern
        DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimalPlaces));
        String formattedSaving = decimalFormat.format(totalSaving);
        Double resultSaving = Double.parseDouble(formattedSaving);
        RecentContribution recentContribution1 = new RecentContribution("Total saving", today, resultSaving);

        String formattedSocial = decimalFormat.format(totalSocialFund-totalSocialFundReleased);
        Double resultSocial = Double.parseDouble(formattedSocial);
        RecentContribution recentContribution2 = new RecentContribution("Total Social Fund", today, resultSocial);

        String formattedLD = decimalFormat.format(totalLoanDespered);
        Double resultLD = Double.parseDouble(formattedLD);
        RecentContribution recentContribution3 = new RecentContribution("Total Loan Disbursed", today, resultLD);

        String formattedLR = decimalFormat.format(totalLoanRepaid);
        Double resultLR = Double.parseDouble(formattedLR);
        RecentContribution recentContribution4 = new RecentContribution("Total Loan Repayed", today, resultLR);

        String formattedSR = decimalFormat.format(totalSocialFundReleased);
        Double resultSR = Double.parseDouble(formattedSR);
        RecentContribution recentContribution5 = new RecentContribution("Total Social Fund released", today, resultSR);
        recentContributions.add(recentContribution1);
        recentContributions.add(recentContribution2);
        recentContributions.add(recentContribution3);
        recentContributions.add(recentContribution4);
        recentContributions.add(recentContribution5);

        // Format the double value to a string with the specified number of decimal
        // places
        String formatted = decimalFormat
                .format(totalSaving - totalLoanDespered + totalLoanRepaid + totalPenalityAmount-totalExpenditure);
        Double result = Double.parseDouble(formatted);
        homepageResponse.setTotalAmount(result);
        Double netSocialFund=totalSocialFund - totalSocialFundReleased;
        homepageResponse.setTotalSocialFund(netSocialFund);
        homepageResponse.setMilestone(milestone());
        homepageResponse.setRecentContributions(recentContributions);
        homepageResponse.setTipOfTheDay(tipOfTheDay());

        return homepageResponse;
    }

    private TipOfTheDay tipOfTheDay() {
        TipOfTheDay tipOfTheDay = new TipOfTheDay();
        tipOfTheDay.setTitle("Malaria Prevention Tip");
        tipOfTheDay.setDescription("Use mosquito nets at night to protect yourself from malaria-carrying mosquitoes.");

        return tipOfTheDay;
    }

    double totalSaving2 = 0;

    private Milestone milestone() {
        totalSaving2 = 0;
        Users user = loggedInUser.getUser();
        List<Transaction> transactions = transactionRepository.findTransactionByGroup(user.getGroup());
        transactions.stream().forEach(t -> {
            if (t.getPaymentType().getPaymentTypeId() == 1) {
                totalSaving2 += t.getAmount();
            }
        });
        int bronze = 0;
        int silver = 0;
        int gold = 0;
        int premium = 0;
        if (totalSaving2 <= 5000) {
            bronze = 1;
            silver = 0;
            gold = 0;
            premium = 0;
        }
        if (totalSaving2 > 5000 && totalSaving2 <= 7500) {
            bronze = 2;
            silver = 1;
            gold = 0;
            premium = 0;
        }
        if (totalSaving2 > 7500 && totalSaving2 <= 10000) {
            bronze = 2;
            silver = 2;
            gold = 1;
            premium = 0;
        }
        if (totalSaving2 > 10000 && totalSaving2 <= 15000) {
            bronze = 2;
            silver = 2;
            gold = 2;
            premium = 1;
        }
        if (totalSaving2 > 15000) {
            bronze = 2;
            silver = 2;
            gold = 2;
            premium = 2;
        }
        return new Milestone(bronze, silver, gold, premium);
    }

}
