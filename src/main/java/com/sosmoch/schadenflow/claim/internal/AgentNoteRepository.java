package com.sosmoch.schadenflow.claim.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface AgentNoteRepository extends JpaRepository<AgentNote, UUID> {

    List<AgentNote> findByClaimIdOrderByCreatedAtDesc(UUID claimId);

}
