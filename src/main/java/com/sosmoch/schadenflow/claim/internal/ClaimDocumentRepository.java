package com.sosmoch.schadenflow.claim.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, UUID> {
    List<ClaimDocument> findByClaimId(UUID claimId);
}
