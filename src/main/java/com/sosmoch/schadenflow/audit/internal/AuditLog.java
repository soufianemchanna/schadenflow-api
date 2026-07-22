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

    @Column(nullable = false)
    private String entityType;

    @Column(nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    private String performedAction; //TODO: Determining what action as enum (in future)

    private String details;

    @Column(nullable = false)
    private String performedBy; //TODO: by email or name of user, to see

    @Column(length = 500) private String oldValue;
    @Column(length = 500) private String newValue;

    @Column(nullable = false)
    private LocalDateTime performedAt;

    @PrePersist void onCreate() {performedAt = LocalDateTime.now();}
}
