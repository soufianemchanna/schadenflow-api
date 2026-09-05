package com.sosmoch.schadenflow.insuredclient.internal.client;

import com.sosmoch.schadenflow.core.database.SoftDeletable;
import com.sosmoch.schadenflow.insuredclient.enums.Gender;
import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "insured_clients")
@SQLDelete(sql = "UPDATE insured_clients SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InsuredClient extends SoftDeletable {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String clientNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private UUID userId;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false) private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuredClientStatus status = InsuredClientStatus.PENDING_REGISTRATION;

    private LocalDateTime registeredAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
