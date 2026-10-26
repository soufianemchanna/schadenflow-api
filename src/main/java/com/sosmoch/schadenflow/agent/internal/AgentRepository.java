package com.sosmoch.schadenflow.agent.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

interface AgentRepository extends JpaRepository<Agent, UUID>, JpaSpecificationExecutor<Agent> {

    Optional<Agent> findByUserId(UUID userId);
    Optional<Agent> findByEmail(String email);

    Page<Agent> findByAvailableAndSpecialization(Boolean available, Specialization specialization, Pageable pageable);


}
