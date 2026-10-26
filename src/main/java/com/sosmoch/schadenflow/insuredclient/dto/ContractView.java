package com.sosmoch.schadenflow.insuredclient.dto;

import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import com.sosmoch.schadenflow.insuredclient.enums.ProductType;
import com.sosmoch.schadenflow.insuredclient.internal.contract.Contract;

import java.time.LocalDate;
import java.util.UUID;

public record ContractView(
        UUID id,
        String contractNumber,
        UUID insuredClientId,
        ProductType productType,
        ContractStatus status,
        LocalDate startDate,
        LocalDate endDate
) {
    public static ContractView from(Contract c) {
        return new ContractView(
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
