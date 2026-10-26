package com.sosmoch.schadenflow.insuredclient.internal.contract;

import com.sosmoch.schadenflow.core.job.AsyncJob;
import com.sosmoch.schadenflow.core.job.AsyncJobDto;
import com.sosmoch.schadenflow.insuredclient.*;
import com.sosmoch.schadenflow.insuredclient.dto.ContractView;
import com.sosmoch.schadenflow.insuredclient.dto.CreateContractRequest;
import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import com.sosmoch.schadenflow.insuredclient.exceptions.*;
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

import java.time.LocalDate;
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
    public ContractView createContract(CreateContractRequest request) {
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
    public ContractView getContract(UUID id) {
        return toContractResponse(contractRepository.findById(id).orElseThrow(
                () -> new ContractNotFoundException(id.toString())
        ));
    }

    @Override
    public List<ContractView> getContractsForClient(UUID insuredClientId) {
        return contractRepository.findByInsuredClientId(insuredClientId).stream()
                .map(this::toContractResponse).toList();
    }

    @Override
    public ContractView validateContractForClaim(UUID contractId, UUID insuredClientId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(
                () -> new ContractNotFoundException(contractId.toString())
        );

        if(!contract.getInsuredClient().getId().equals(insuredClientId)){
            throw new ContractOwnershipMismatchException(contractId, insuredClientId);
        }

        if(contract.getStatus() != ContractStatus.ACTIVE){
            throw new InactiveContractException(contractId.toString());
        }
        return toContractResponse(contract);
    }

    @Override
    public ContractView suspendFromExternalSystem(String contractNumber, String reason) {
        Contract contract = contractRepository.findByContractNumber(contractNumber).orElseThrow(
                () -> new ContractNotFoundException(contractNumber)
        );

        if(contract.getStatus() != ContractStatus.ACTIVE){
            throw new InactiveContractException(contractNumber);
        }

        contract.setStatus(ContractStatus.SUSPENDED);
        return toContractResponse(contractRepository.save(contract));
    }

    @Override
    public ContractView reactiveFromExternalSystem(String contractNumber, LocalDate newEndDate) {
        Contract contract = contractRepository.findByContractNumber(contractNumber).orElseThrow(
                () -> new ContractNotFoundException(contractNumber)
        );

        if(contract.getStatus() != ContractStatus.SUSPENDED && contract.getStatus() != ContractStatus.EXPIRED){
            throw new InvalidContractTransitionException(contractNumber, contract.getStatus(), ContractStatus.ACTIVE);
        }

        if(contract.getStatus() == ContractStatus.EXPIRED){
            contract.setEndDate(newEndDate != null ? newEndDate : LocalDate.now().plusYears(1));
        }

        contract.setStatus(ContractStatus.ACTIVE);
        return toContractResponse(contractRepository.save(contract));

    }

    public UUID startContractImport(byte[] fileContent){
        AsyncJob job = csvImportService.createJob();
        csvImportService.processCsv(job.getId(), fileContent, contractCsvImportStrategy);
        return job.getId();
    }

    public AsyncJobDto getContractsImportStatus(UUID jobId){
        return csvImportService.getContractsImportStatus(jobId);
    }

    public Page<ContractView> getAll(Pageable pageable){
        return contractRepository.findAll(pageable).map(this::toContractResponse);
    }

    public ContractView updateContractStatus(UUID id, ContractStatus status){
        Contract contract = contractRepository.findById(id).orElseThrow(
                () -> new ContractNotFoundException(id.toString())
        );
        contract.setStatus(status);
        return toContractResponse(contractRepository.save(contract));
    }

    private ContractView toContractResponse(Contract c) {
        return new ContractView(
                c.getId(), c.getContractNumber(), c.getInsuredClient().getId(),
                c.getProductType(), c.getStatus(), c.getStartDate(), c.getEndDate()
        );
    }
}
