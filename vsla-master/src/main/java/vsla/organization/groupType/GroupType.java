package vsla.organization.groupType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import vsla.organization.organization.Organization;
import vsla.utils.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_types")
@SQLDelete(sql = "UPDATE group_types SET deleted = true WHERE group_type_id=?")
@Where(clause = "deleted=false")
@Data
public class GroupType {

    @Id
    @Column(name = "group_type_id")
    private Long groupTypeId;

    @Column(name = "group_type_name", nullable = false)
    private String groupTypeName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
