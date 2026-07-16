package com.sosmoch.schadenflow.claim.internal;

import com.sosmoch.schadenflow.claim.Claim;
import com.sosmoch.schadenflow.core.database.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "agent_notes")
@SQLDelete(sql = "UPDATE agent_notes SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AgentNote extends SoftDeletable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    @ToString.Exclude
    private Claim claim;

    @Column(nullable = false)
    private UUID agentId;

    @Column(nullable = false, length = 2000)
    private String content;

    private LocalDateTime createdAt;

    @PrePersist void onCreate() {createdAt = LocalDateTime.now();}
}
