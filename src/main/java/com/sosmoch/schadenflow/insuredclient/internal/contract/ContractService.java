package com.sosmoch.schadenflow.insuredclient.internal.contract;

import com.sosmoch.schadenflow.core.job.AsyncJob;
import com.sosmoch.schadenflow.core.job.AsyncJobDto;
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
import com.sosmoch.schadenflow.insuredclient.internal.csv.ContractCsvImportStrategy;
import com.sosmoch.schadenflow.insuredclient.internal.csv.CsvImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CsvImportService csvImportService;
    private final ContractCsvImportStrategy contractCsvImportStrategy;

    @Override
    @Transactional
    public ContractResponse createContract(CreateContractRequest request) {
        InsuredClient client = insuredClientRepository.findInsuredClientByClientNumber(request.clientNumber()).orElseThrow(
                () -> new InsuredClientNotFoundException(request.clientNumber())
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

    public UUID startContractImport(byte[] fileContent){
        AsyncJob job = csvImportService.createJob();
        csvImportService.processCsv(job.getId(), fileContent, contractCsvImportStrategy);
        return job.getId();
    }

    public AsyncJobDto getContractsImportStatus(UUID jobId){
        return csvImportService.getContractsImportStatus(jobId);
    }

    public Page<ContractResponse> getAll(Pageable pageable){
        return contractRepository.findAll(pageable).map(this::toContractResponse);
    }

    private ContractResponse toContractResponse(Contract c) {
        return new ContractResponse(
                c.getId(), c.getContractNumber(), c.getInsuredClient().getId(),
                c.getProductType(), c.getStatus(), c.getStartDate(), c.getEndDate()
        );
    }
}
