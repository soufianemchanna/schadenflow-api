package com.sosmoch.schadenflow.claim.internal;

import com.sosmoch.schadenflow.claim.Claim;
import com.sosmoch.schadenflow.claim.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

interface ClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findByInsuredClientId(UUID insuredClientId);
    List<Claim> findByAssignedAgentId(UUID assignedAgentId);
    List<Claim> findByStatus(ClaimStatus status);
    List<Claim> findByInsuredClientIdAndStatus(UUID insuredClientId, ClaimStatus status);

    long countByAssignedAgentIdAndStatusIn(UUID assignedAgentId, Collection<ClaimStatus> statuses);


    @Query("""
        SELECT c.assignedAgentId as agentId, COUNT(c) as total
        FROM Claim c
        WHERE c.assignedAgentId IN :assignedAgentIds AND c.status IN :statuses
        GROUP BY c.assignedAgentId
    """
    )
    List<AgentClaimCount> countByAssignedAgentIdInAndStatusIn(@Param("assignedAgentIds") Collection<UUID> assignedAgentIds, @Param("statuses") Collection<ClaimStatus> statuses);
    interface AgentClaimCount{
        UUID getAgentId();
        Long getTotal();
    }
}
