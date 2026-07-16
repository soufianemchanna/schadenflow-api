package com.sosmoch.schadenflow.notification;

import com.sosmoch.schadenflow.core.database.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends SoftDeletable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID recipientId; // user_id

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String type; //TODO: should be enum : "CLAIM_SUBMITTED", "CLAIM_VALIDATED", etc

    @Column(nullable = false)
    private UUID claimId;

    @Builder.Default private boolean read = false;

    private LocalDateTime createdAt;

    @PrePersist void onCreate() {createdAt = LocalDateTime.now();}
}
