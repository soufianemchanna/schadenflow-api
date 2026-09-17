package com.sosmoch.schadenflow.claim;


import com.sosmoch.schadenflow.core.database.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "claims")
@SQLDelete(sql = "UPDATE claims SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class Claim extends SoftDeletable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID insuredClientId;

    @Column(nullable = false)
    private UUID contractId;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 2000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;

    private UUID assignedAgentId;

    private String rejectionReason; // extendable by creating claims_responses table

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist void onCreate() {
        submittedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(status == null) status = ClaimStatus.SUBMITTED;
    }

    @PreUpdate void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
