package com.sosmoch.schadenflow.insuredclient.internal;

import com.sosmoch.schadenflow.insuredclient.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.InsuredClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface InsuredClientRepository extends JpaRepository<InsuredClient, UUID> {

    Optional<InsuredClient> findInsuredClientByClientNumberAndDateOfBirthAndEmail(String clientNumber, String dateOfBirth, String email);
    Optional<InsuredClient> findAllByEmail(String email);
    Optional<InsuredClient> findByUserId(UUID userId);

    boolean existsByClientNumber(String clientNumber);

    List<InsuredClient> findByStatus(InsuredClientStatus status);

}
