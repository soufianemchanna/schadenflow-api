package com.sosmoch.schadenflow.insuredclient.internal.client;


import com.sosmoch.schadenflow.core.job.AsyncJobDto;
import com.sosmoch.schadenflow.core.job.AsyncJobStatus;
import com.sosmoch.schadenflow.insuredclient.dto.CreateInsuredClientRequest;
import com.sosmoch.schadenflow.insuredclient.dto.InsuredClientRepsonse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/insured-client")
@Tag(name = "Client", description = "Insured client operations")
@RequiredArgsConstructor
public class InsuredClientController {
    private final InsuredClientService insuredClientService;

    // create by admin BULK(CSV)
    @PostMapping("/bulk-import")
    public AsyncJobDto startImport(@RequestParam MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        UUID jobId = insuredClientService.startClientImport(fileContent);
        return new AsyncJobDto(jobId, AsyncJobStatus.PROCESSING);
    }

    @GetMapping("/bulk-import/status/{jobId}")
    public AsyncJobDto getImportStatus(@PathVariable UUID jobId){
        return insuredClientService.getClientImportStatus(jobId);
    }

    // create by admin MANUALLY(one client - ui)
    @PostMapping("/create-client")
    public InsuredClientRepsonse createSingleClient(@RequestBody CreateInsuredClientRequest request){
        return insuredClientService.createInsuredClient(request);
    }

}
