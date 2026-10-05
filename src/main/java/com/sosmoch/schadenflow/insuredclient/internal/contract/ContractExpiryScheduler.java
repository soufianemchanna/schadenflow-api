package com.sosmoch.schadenflow.insuredclient.internal.contract;


import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractExpiryScheduler {
    private final ContractRepository contractRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void expireOverdueContracts(){
        List<Contract> toExpire = contractRepository
                .findByStatusAndEndDateBefore(ContractStatus.ACTIVE, LocalDate.now());
        for(Contract contract : toExpire){
            contract.setStatus(ContractStatus.EXPIRED);
            contractRepository.save(contract);
        }
    }

}
