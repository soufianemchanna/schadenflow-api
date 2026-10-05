package com.sosmoch.schadenflow.insuredclient.internal.contract;


import com.sosmoch.schadenflow.insuredclient.ContractFacade;
import com.sosmoch.schadenflow.insuredclient.dto.ContractResponse;
import com.sosmoch.schadenflow.insuredclient.dto.SuspendContractRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/external/contracts")
@RequiredArgsConstructor
public class ContractIntegrationController {
    private final ContractFacade contractFacade;
    // update contract status, extend end_date

    @PatchMapping("/{contractNumber}/suspend")
    public ContractResponse suspendContract(@PathVariable String contractNumber,@Valid @RequestBody SuspendContractRequest request){
        return contractFacade.suspendFromExternalSystem(contractNumber, request.reason());
    }

    @PatchMapping("/{contractNumber}/reactivate")
    public ContractResponse reactivateContract(@PathVariable String contractNumber, @Valid @RequestBody LocalDate newEndDate){
        return contractFacade.reactiveFromExternalSystem(contractNumber, newEndDate);
    }
}
