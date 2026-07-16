package com.sosmoch.schadenflow.claim.internal;

import com.sosmoch.schadenflow.claim.Claim;
import com.sosmoch.schadenflow.claim.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface ClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findByInsuredClientId(UUID insuredClientId);
    List<Claim> findByAssignedAgentId(UUID assignedAgentId);
    List<Claim> findByStatus(ClaimStatus status);
    List<Claim> findByInsuredClientIdAndStatus(UUID insuredClientId, ClaimStatus status);

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status NOT IN ('REJECTED','CLOSED','RESOLVED')")
    long countActiveClaims();
}
