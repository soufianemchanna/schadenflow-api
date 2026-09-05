package com.sosmoch.schadenflow.insuredclient.dto;

import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull InsuredClientStatus status
) {
}
