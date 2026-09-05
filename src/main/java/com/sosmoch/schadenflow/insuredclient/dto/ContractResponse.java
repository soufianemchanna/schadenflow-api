package com.sosmoch.schadenflow.insuredclient.dto;

import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import com.sosmoch.schadenflow.insuredclient.enums.ProductType;

import java.time.LocalDate;
import java.util.UUID;

public record ContractResponse(
        UUID id,
        String contractNumber,
        UUID insuredClientId,
        ProductType productType,
        ContractStatus status,
        LocalDate startDate,
        LocalDate endDate
) {
}
