package com.sosmoch.schadenflow.insuredclient.dto;

import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import com.sosmoch.schadenflow.insuredclient.enums.ProductType;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.internal.contract.Contract;

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
    public static ContractResponse from(Contract c) {
        return new ContractResponse(
                c.getId(),
                c.getContractNumber(),
                c.getInsuredClient().getId(),
                c.getProductType(),
                c.getStatus(),
                c.getStartDate(),
                c.getEndDate()
        );
    }
}
