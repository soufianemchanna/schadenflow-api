package com.sosmoch.schadenflow.audit.internal;

import com.sosmoch.schadenflow.core.database.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "audit_logs")
@SQLDelete(sql = "UPDATE audit_logs SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends SoftDeletable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String entityType;
    private UUID entityId;
    private String action;
    private String details;
    private String performedBy; //TODO: by email or name of user, to see

    @Column(length = 500) private String oldValue;
    @Column(length = 500) private String newValue;

    private LocalDateTime performedAt;

    @PrePersist void onCreate() {performedAt = LocalDateTime.now();}
}
