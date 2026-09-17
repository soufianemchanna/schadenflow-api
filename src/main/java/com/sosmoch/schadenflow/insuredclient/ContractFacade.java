package com.sosmoch.schadenflow.insuredclient;

import com.sosmoch.schadenflow.insuredclient.dto.ContractResponse;
import com.sosmoch.schadenflow.insuredclient.dto.CreateContractRequest;

import java.util.List;
import java.util.UUID;

public interface ContractFacade {
    ContractResponse createContract(CreateContractRequest request);
    ContractResponse getContract(UUID id);
    List<ContractResponse> getContractsForClient(UUID insuredClientId);
    // Used by claim module at submission time
    ContractResponse validateContractForClaim(UUID contractId, UUID insuredClientId);
}
