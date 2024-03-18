package vsla.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vsla.organization.groupType.GroupType;
import vsla.organization.organization.Organization;
import vsla.organization.project.Project;
import vsla.userManager.address.Address;
import vsla.userManager.user.Users;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "groups")
@Data
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "group_size", nullable = false)
    private Integer groupSize;

    @Column(name="round")
    private Integer currentRound;

    @Column(name = "entry_fee", nullable = false)
    private BigDecimal entryFee;
    @Column(name = "is_Active", nullable = false)
    private Boolean isActive;

    @Column(name = "share_Amount")
    private Double shareAmount;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "social_fund_amount")
    private Double socialFundAmount;

    @OneToOne
    @JoinColumn(name = "group_admin_id")
    private Users groupAdmin;

    @ManyToOne
    @JoinColumn(name = "groupType_id", nullable = false)
    private GroupType groupType;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public void setGroupAdmin(Users groupAdmin) {
        if (groupAdmin != null && groupAdmin.getRole().getRoleName().equalsIgnoreCase("GROUP_ADMIN"))
            this.groupAdmin = groupAdmin;
        else
            // Handle the case where the user doesn't have the "admin" role.
            throw new IllegalArgumentException("The user must have the 'GROUP_ADMIN' role to be a group admin.");
    }
}
