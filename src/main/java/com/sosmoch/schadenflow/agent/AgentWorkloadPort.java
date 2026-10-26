package com.sosmoch.schadenflow.agent;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface AgentWorkloadPort {
    long countActiveClaims(UUID agentId);
    Map<UUID, Long> countActiveClaims(Collection<UUID> agentIds);
}
