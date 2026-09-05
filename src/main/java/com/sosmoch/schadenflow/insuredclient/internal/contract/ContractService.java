package com.sosmoch.schadenflow.insuredclient.internal.contract;

import com.sosmoch.schadenflow.insuredclient.*;
import com.sosmoch.schadenflow.insuredclient.dto.ContractResponse;
import com.sosmoch.schadenflow.insuredclient.dto.CreateContractRequest;
import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import com.sosmoch.schadenflow.insuredclient.exceptions.ContractNotFoundException;
import com.sosmoch.schadenflow.insuredclient.exceptions.ContractOwnershipMismatchException;
import com.sosmoch.schadenflow.insuredclient.exceptions.InactiveContractException;
import com.sosmoch.schadenflow.insuredclient.exceptions.InsuredClientNotFoundException;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService implements ContractFacade {
    private final ContractRepository contractRepository;
    private final InsuredClientRepository insuredClientRepository;

    @Override
    @Transactional
    public ContractResponse createContract(UUID insuredClientId, CreateContractRequest request) {
        InsuredClient client = insuredClientRepository.findById(insuredClientId).orElseThrow(
                () -> new InsuredClientNotFoundException(insuredClientId.toString())
        );

        Contract contract = Contract.builder()
                .contractNumber(request.contractNumber())
                .insuredClient(client)
                .productType(request.productType())
                .status(ContractStatus.ACTIVE)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        return toContractResponse(contractRepository.save(contract));
    }

    @Override
    public ContractResponse getContract(UUID id) {
        return toContractResponse(contractRepository.findById(id).orElseThrow(
                () -> new ContractNotFoundException(id)
        ));
    }

    @Override
    public List<ContractResponse> getContractsForClient(UUID insuredClientId) {
        return contractRepository.findByInsuredClientId(insuredClientId).stream()
                .map(this::toContractResponse).toList();
    }

    @Override
    public ContractResponse validateContractForClaim(UUID contractId, UUID insuredClientId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(
                () -> new ContractNotFoundException(contractId)
        );

        if(!contract.getInsuredClient().getId().equals(insuredClientId)){
            throw new ContractOwnershipMismatchException(contractId, insuredClientId);
        }

        if(contract.getStatus() != ContractStatus.ACTIVE){
            throw new InactiveContractException(contractId);
        }
        return toContractResponse(contract);
    }

    private ContractResponse toContractResponse(Contract c) {
        return new ContractResponse(
                c.getId(), c.getContractNumber(), c.getInsuredClient().getId(),
                c.getProductType(), c.getStatus(), c.getStartDate(), c.getEndDate()
        );
    }
}
