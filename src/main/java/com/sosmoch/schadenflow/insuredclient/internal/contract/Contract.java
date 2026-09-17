package com.sosmoch.schadenflow.insuredclient.internal.contract;


import com.sosmoch.schadenflow.core.database.SoftDeletable;
import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contracts")
@SQLDelete(sql = "UPDATE contracts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract extends SoftDeletable {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String contractNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private InsuredClient insuredClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status = ContractStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist void onCreate() {createdAt = LocalDateTime.now();}
    @PreUpdate void onUpdate() {updatedAt = LocalDateTime.now();}
}
