package com.sosmoch.schadenflow.agent.dto;

import com.sosmoch.schadenflow.agent.internal.Specialization;

public record UpdateAgentRequest(
        String agentName,
        Specialization specialization
) {
}
