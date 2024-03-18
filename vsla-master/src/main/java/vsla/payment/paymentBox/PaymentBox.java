package vsla.payment.paymentBox;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vsla.group.Group;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_box")
@Data
public class PaymentBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_box_id")
    private Long paymentBoxId;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
