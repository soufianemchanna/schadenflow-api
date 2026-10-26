package com.sosmoch.schadenflow.insuredclient.internal.contract;

import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findByInsuredClientId(UUID insuredClientId);

    List<Contract> findByStatusAndEndDateBefore(ContractStatus contractStatus, LocalDate now);
    Optional<Contract> findByContractNumber(String contractNumber);
}
