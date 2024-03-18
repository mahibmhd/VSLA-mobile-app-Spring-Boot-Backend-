package vsla.loan;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import vsla.group.Group;
import vsla.userManager.user.Users;

@Entity
@Table(name = "loan")
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "interest")
    private Double interest;

    @Column(name = "days")
    private int days;

    @Column(name = "amount_To_Pay")
    private Double amountToPay;

    @Column(name = "paid_amount")
    private Double paidAmount;


     @Column(name = "description")
    private String description;
     @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;


     @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users loanRequester;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern="yyyy-MM-dd",shape = Shape.STRING)
    @Column(name = "due_Date")
    private String dueDate;
}
