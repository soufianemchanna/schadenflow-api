package com.sosmoch.schadenflow.insuredclient.internal.client;


import com.sosmoch.schadenflow.core.job.AsyncJob;
import com.sosmoch.schadenflow.core.job.AsyncJobDto;
import com.sosmoch.schadenflow.insuredclient.InsuredClientFacade;
import com.sosmoch.schadenflow.insuredclient.dto.*;
import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;
import com.sosmoch.schadenflow.insuredclient.exceptions.InsuredClientNotFoundException;
import com.sosmoch.schadenflow.insuredclient.internal.csv.ClientCsvImportStrategy;
import com.sosmoch.schadenflow.insuredclient.internal.csv.CsvImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuredClientService implements InsuredClientFacade {
    private final InsuredClientRepository insuredClientRepository;
    private final CsvImportService csvImportService;
    private final ClientCsvImportStrategy clientCsvImportStrategy;
    @Override
    public Optional<InsuredClientView> verifyClientHasIdentity(String clientNumber, String email) {
        InsuredClient client = insuredClientRepository.
                findInsuredClientByClientNumberAndEmail(clientNumber, email)
                .orElseThrow(() -> new InsuredClientNotFoundException(clientNumber));
        return Optional.of(new InsuredClientView(client.getId(),
                client.getUserId(),
                client.getClientNumber()));
    }

    @Override
    public void activateClientAccount(UUID userId, UUID insuredClientId) {
        InsuredClient client = insuredClientRepository.findById(insuredClientId)
                        .orElseThrow(() -> new InsuredClientNotFoundException(insuredClientId.toString()));
        client.setUserId(userId);
        client.setStatus(InsuredClientStatus.ACTIVE);
        client.setRegisteredAt(LocalDateTime.now());
        insuredClientRepository.save(client);
    }

    @Override
    public void createInsuredClients(List<CreateInsuredClientRequest> clients) {
        List<InsuredClient> clientList = clients.stream().map(
                request -> InsuredClient.builder()
                        .clientNumber(request.clientNumber())
                        .fullName(request.fullName())
                        .gender(request.gender())
                        .dateOfBirth(request.dateOfBirth())
                        .email(request.email())
                        .status(request.status())
                        .build()
        ).toList();

        insuredClientRepository.saveAll(clientList);
    }

    @Override
    public long countInsuredClients() {
        return insuredClientRepository.count();
    }

    @Override
    public void linkUserAccount(UUID userId, String clientNumber) {
        InsuredClient client = insuredClientRepository.findInsuredClientByClientNumber(clientNumber)
                .orElseThrow(() -> new InsuredClientNotFoundException(clientNumber));
        client.setUserId(userId);
        client.setRegisteredAt(LocalDateTime.now());
        insuredClientRepository.save(client);
    }


    public UUID startClientImport(byte[] fileContent){
        AsyncJob job = csvImportService.createJob();
        csvImportService.processCsv(job.getId(), fileContent, clientCsvImportStrategy);
        return job.getId();
    }

    public AsyncJobDto getClientImportStatus(UUID jobId){
        return csvImportService.getContractsImportStatus(jobId);
    }

    public InsuredClientRepsonse createInsuredClient(CreateInsuredClientRequest request) {
        InsuredClient client = InsuredClient.builder()
                        .clientNumber(request.clientNumber())
                        .fullName(request.fullName())
                        .gender(request.gender())
                        .dateOfBirth(request.dateOfBirth())
                        .email(request.email())
                        .status(request.status())
                        .build();

        return toInsuredClientResponse(insuredClientRepository.save(client));
    }

    private InsuredClientRepsonse toInsuredClientResponse(InsuredClient insuredClient){
        return InsuredClientRepsonse.from(insuredClient);
    }
}

