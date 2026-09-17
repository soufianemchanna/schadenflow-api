package com.sosmoch.schadenflow.insuredclient.internal.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosmoch.schadenflow.core.job.*;
import com.sosmoch.schadenflow.insuredclient.dto.CsvImportResult;
import com.sosmoch.schadenflow.insuredclient.dto.CsvRowError;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CsvImportService {
    private final AsyncJobRepository asyncJobRepository;
    private final CsvImportRunner csvImportRunner;
    private final ObjectMapper objectMapper;


    @Async("csvImportExecutor")
    public <T> void processCsv(UUID jobId, byte[] fileContent, CsvImportStrategy<T> strategy){
        try {
            CsvImportResult result = csvImportRunner.run(fileContent, strategy);
            AsyncJob job = asyncJobRepository.findById(jobId).orElseThrow();
            job.setStatus(AsyncJobStatus.COMPLETED);
            job.setFinishedAt(Instant.now());
            job.setSuccessCount(result.createdRows());
            job.setTotalCount(result.totalRows());
            job.setErrorsJson(!result.errors().isEmpty() ? result.errors().toString() : null);
            asyncJobRepository.save(job);
        } catch (Exception e) {
            AsyncJob job = asyncJobRepository.findById(jobId).orElseThrow();
            job.setStatus(AsyncJobStatus.FAILED);
            job.setFinishedAt(Instant.now());
            job.setErrorsJson(e.getMessage());
            asyncJobRepository.save(job);
        }
    }

    public AsyncJob createJob(){
        AsyncJob job = new AsyncJob();
        job.setStatus(AsyncJobStatus.PROCESSING);
        job.setStartedAt(Instant.now());
        asyncJobRepository.save(job);
        return job;
    }

    public AsyncJobDto getContractsImportStatus(UUID jobId){
        AsyncJob job = asyncJobRepository.findById(jobId).orElseThrow(
                () -> new AsyncJobNotFoundException("Job with id " + jobId + " not found")
        );
        return new AsyncJobDto(job.getId(), job.getStatus());
    }

}
