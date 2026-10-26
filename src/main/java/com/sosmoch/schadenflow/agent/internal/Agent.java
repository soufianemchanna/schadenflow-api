package com.sosmoch.schadenflow.agent.internal;

import com.sosmoch.schadenflow.core.database.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.generator.EventType;
import org.hibernate.annotations.Generated;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "agents")
@SQLDelete(sql = "UPDATE agents SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent extends SoftDeletable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @Column(name = "agent_number", insertable = false, updatable = false)
    @Generated(event = EventType.INSERT)
    private String agentNumber;

    @Column(nullable = false)
    private String agentName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Builder.Default private boolean available = true;
    @Builder.Default private boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){createdAt = LocalDateTime.now();}
}
