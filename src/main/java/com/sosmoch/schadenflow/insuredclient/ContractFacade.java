package com.sosmoch.schadenflow.insuredclient;

import com.sosmoch.schadenflow.insuredclient.dto.ContractView;
import com.sosmoch.schadenflow.insuredclient.dto.CreateContractRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ContractFacade {
    ContractView createContract(CreateContractRequest request);
    ContractView getContract(UUID id);
    List<ContractView> getContractsForClient(UUID insuredClientId);
    // Used by claim module at submission time
    ContractView validateContractForClaim(UUID contractId, UUID insuredClientId);
    // Used by external system call
    ContractView suspendFromExternalSystem(String contractNumber, String reason);
    ContractView reactiveFromExternalSystem(String contractNumber, LocalDate newEndDate);
}
