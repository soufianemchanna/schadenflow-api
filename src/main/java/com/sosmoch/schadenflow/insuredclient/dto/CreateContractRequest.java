package com.sosmoch.schadenflow.insuredclient.dto;

import com.sosmoch.schadenflow.insuredclient.enums.ProductType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateContractRequest(
        @NotNull String contractNumber,
        @NotNull ProductType productType,
        @NotNull LocalDate startDate,
        LocalDate endDate
        ) {
}
