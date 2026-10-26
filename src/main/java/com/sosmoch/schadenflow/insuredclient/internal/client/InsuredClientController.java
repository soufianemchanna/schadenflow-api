package com.sosmoch.schadenflow.insuredclient.internal.client;


import com.sosmoch.schadenflow.core.job.AsyncJobDto;
import com.sosmoch.schadenflow.core.job.AsyncJobStatus;
import com.sosmoch.schadenflow.insuredclient.dto.CreateInsuredClientRequest;
import com.sosmoch.schadenflow.insuredclient.dto.InsuredClientRepsonse;
import com.sosmoch.schadenflow.insuredclient.dto.SuspendClientRequest;
import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/insured-clients")
@Tag(name = "Admin / Client", description = "Insured client operations")
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

    // create by admin MANUALLY (one client - ui OR webservice)
    @PostMapping("/create-client")
    public InsuredClientRepsonse createSingleClient(@RequestBody CreateInsuredClientRequest request){
        return insuredClientService.createInsuredClient(request);
    }

    @GetMapping
    public Page<InsuredClientRepsonse> list(
            @RequestParam(required = false) InsuredClientStatus status,
            @PageableDefault(size = 20, sort = "fullName") Pageable pageable
            ){
        return insuredClientService.getAll(status, pageable);
    }

    @PatchMapping("/status/{id}/suspend")
    public InsuredClientRepsonse suspendClient(@PathVariable UUID id, @RequestBody SuspendClientRequest request){
        return insuredClientService.updateStatus(InsuredClientStatus.SUSPENDED, id);
    }

    @PatchMapping("/status/{id}/reactive")
    public InsuredClientRepsonse reactiveClient(@PathVariable UUID id){
        return insuredClientService.updateStatus(InsuredClientStatus.ACTIVE, id);
    }


}
