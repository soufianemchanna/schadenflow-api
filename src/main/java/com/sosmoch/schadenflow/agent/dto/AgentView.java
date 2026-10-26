package com.sosmoch.schadenflow.agent.dto;

import com.sosmoch.schadenflow.agent.internal.Agent;
import com.sosmoch.schadenflow.agent.internal.Specialization;

import java.util.UUID;

public record AgentView(
        UUID id,
        Specialization specialization,
        String name,
        String email,
        UUID userId,
        boolean available,
        String agentNumber,
        long activeClaimsCount
) {
    public static AgentView from(Agent ag, long activeClaimsCount){
        return new AgentView(
                ag.getId(),
                ag.getSpecialization(),
                ag.getAgentName(),
                ag.getEmail(),
                ag.getUserId(),
                ag.isAvailable(),
                ag.getAgentNumber(),
                activeClaimsCount
        );
    }
}
