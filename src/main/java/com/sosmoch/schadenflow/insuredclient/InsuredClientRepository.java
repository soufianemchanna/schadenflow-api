package com.sosmoch.schadenflow.insuredclient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsuredClientRepository extends JpaRepository<InsuredClient, UUID> {

    Optional<InsuredClient> findInsuredClientByClientNumberAndEmail(String clientNumber, String email);
    Optional<InsuredClient> findAllByEmail(String email);
    Optional<InsuredClient> findByUserId(UUID userId);

    boolean existsByClientNumber(String clientNumber);

    List<InsuredClient> findByStatus(InsuredClientStatus status);

}
