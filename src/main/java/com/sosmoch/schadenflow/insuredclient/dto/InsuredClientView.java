package com.sosmoch.schadenflow.insuredclient.dto;

import java.util.UUID;

public record InsuredClientView(
        UUID id,
        UUID userId,
        String clientNumber
) {
}
