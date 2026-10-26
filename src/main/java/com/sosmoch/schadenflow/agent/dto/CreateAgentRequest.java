package com.sosmoch.schadenflow.agent.dto;

import com.sosmoch.schadenflow.agent.internal.Specialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateAgentRequest(
    @NotNull String agentName,
    @NotNull @Email String email,
    @NotNull Specialization specialization,
    @NotNull String inialPassword

) {
}
