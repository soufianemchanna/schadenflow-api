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
@Table(name = "claim_documents")
@SQLDelete(sql = "UPDATE claim_documents SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDocument extends SoftDeletable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    @ToString.Exclude
    private Claim claim;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String bucketName;

    @Column(nullable = false)
    private String mimeType;

    private Long fileSizeBytes;

    @Column(nullable = false)
    private String objectKey;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @PrePersist void onCreate() {uploadedAt = LocalDateTime.now();}

}
