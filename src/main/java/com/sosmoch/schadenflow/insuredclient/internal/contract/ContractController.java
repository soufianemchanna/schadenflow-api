package com.sosmoch.schadenflow.insuredclient.internal.contract;


import com.sosmoch.schadenflow.core.job.AsyncJobDto;
import com.sosmoch.schadenflow.core.job.AsyncJobStatus;
import com.sosmoch.schadenflow.insuredclient.dto.ContractResponse;
import com.sosmoch.schadenflow.insuredclient.dto.CreateContractRequest;
import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
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
@RequestMapping("/api/admin/contracts")
@RequiredArgsConstructor
@Tag(name = "Admin / Contract", description = "Contract operations")
public class ContractController {
    private final ContractService contractService;

    // create by admin BULK(CSV)
    @PostMapping("/bulk-import")
    public AsyncJobDto startImport(@RequestParam MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        UUID jobId = contractService.startContractImport(fileContent);
        return new AsyncJobDto(jobId, AsyncJobStatus.PROCESSING);
    }

    @GetMapping("/bulk-import/status/{jobId}")
    public AsyncJobDto getImportStatus(@PathVariable UUID jobId){
        return contractService.getContractsImportStatus(jobId);
    }

    // create by admin MANUALLY(one contract - ui OR webservice)
    @PostMapping("/create-contract")
    public ContractResponse createSingleContract(@RequestBody CreateContractRequest request){
        return contractService.createContract(request);
    }

    @PatchMapping("/status/{id}/cancel")
    public ContractResponse cancelSingleContract(@PathVariable UUID id){
        return contractService.updateContractStatus(id, ContractStatus.CANCELLED);
    }

    @GetMapping
    public Page<ContractResponse> list(
            @PageableDefault(size = 20, sort = "startDate") Pageable pageable
    ){
        return contractService.getAll(pageable);
    }

}
