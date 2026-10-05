package com.sosmoch.schadenflow.insuredclient.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record ReactiveContractRequest(
        @Nullable LocalDate endDate
) {
}
