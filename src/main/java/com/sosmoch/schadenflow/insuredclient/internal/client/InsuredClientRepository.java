package com.sosmoch.schadenflow.insuredclient.internal.client;

import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsuredClientRepository extends JpaRepository<InsuredClient, UUID> {

    Optional<InsuredClient> findInsuredClientByClientNumberAndEmail(String clientNumber, String email);
    Optional<InsuredClient> findInsuredClientByClientNumber(String clientNumber);
    Optional<InsuredClient> findAllByEmail(String email);
    Optional<InsuredClient> findByUserId(UUID userId);

    boolean existsByClientNumber(String clientNumber);

    List<InsuredClient> findByStatus(InsuredClientStatus status);

}
