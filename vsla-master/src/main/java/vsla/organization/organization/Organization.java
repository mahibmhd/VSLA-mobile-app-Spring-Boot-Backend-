package vsla.organization.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import vsla.organization.project.Project;
import vsla.utils.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
@SQLDelete(sql = "UPDATE organizations SET deleted = true WHERE organization_id=?")
@Where(clause = "deleted=false")
@Data
public class Organization {

    @Id
    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Enumerated(EnumType.STRING)
    private Status organizationStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;

    // @ManyToOne
    // @JsonIgnore
    // @JoinColumn(name = "project_id", nullable = false)
    // private Project project;


}


