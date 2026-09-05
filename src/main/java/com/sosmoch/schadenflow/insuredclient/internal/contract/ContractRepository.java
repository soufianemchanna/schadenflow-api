package com.sosmoch.schadenflow.insuredclient.internal.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findByInsuredClientId(UUID insuredClientId);
}
