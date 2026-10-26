package com.sosmoch.schadenflow.claim.internal;

import com.sosmoch.schadenflow.agent.AgentWorkloadPort;
import com.sosmoch.schadenflow.claim.ClaimStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ClaimAgentWorkloadAdapter implements AgentWorkloadPort {
    private final ClaimRepository claimRepository;
    @Override
    public long countActiveClaims(UUID agentId) {
        return claimRepository.countByAssignedAgentIdAndStatusIn(agentId, ClaimStatus.activeStatus());
    }

    @Override
    public Map<UUID, Long> countActiveClaims(Collection<UUID> agentIds) {
        if (agentIds.isEmpty()) {
            return Map.of();
        }
        Map<UUID, Long> result = new HashMap<>();
        claimRepository.countByAssignedAgentIdInAndStatusIn(agentIds, ClaimStatus.activeStatus())
                .forEach(agentClaimCount -> result.put(agentClaimCount.getAgentId(), agentClaimCount.getTotal()));
        agentIds.forEach(agentId -> result.putIfAbsent(agentId, 0L));
        return result;
    }
}
