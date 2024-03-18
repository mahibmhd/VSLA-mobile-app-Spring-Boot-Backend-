package vsla.homePage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomepageResponse {
    private String groupName;
    private Double totalAmount;
    private Double totalSocialFund;
    private Milestone milestone;
    private TipOfTheDay tipOfTheDay;
    private List<RecentContribution> recentContributions;

}

/**
 * The Milestone class represents a set of milestones with different completion statuses.
 * Milestones can be in one of the following states:
 * - not-started = 0
 * - started = 1
 * - finished = 2
 * This class holds individual counts for bronze, silver, gold, and premium milestones.
 */
@Data
class Milestone {

    private int bronze;
    private int silver;
    private int gold;
    private int premium;

    // Constructor to create an instance of the Milestone class
    public Milestone(int bronze, int silver, int gold, int premium) {
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.premium = premium;
    }
}


@Data
class TipOfTheDay {
    private String title;
    private String description;
}

@Data
class RecentContribution {
    private String contributor;
    private LocalDate date;
    private Double amount;

    public RecentContribution(String contributor, LocalDate date, Double amount) {
        this.contributor = contributor;
        this.date = date;
        this.amount = amount;
    }
}