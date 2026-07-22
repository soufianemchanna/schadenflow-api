package com.sosmoch.schadenflow.agent.internal;

import com.sosmoch.schadenflow.agent.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

interface AgentRepository extends JpaRepository<Agent, UUID> {

    Optional<Agent> findByUserId(UUID userId);
    Optional<Agent> findByEmail(String email);

    @Query("""
            SELECT ag from Agent ag
                WHERE ag.available=true
                    AND (ag.specialization = :type or ag.specialization='ALL')
            ORDER BY ag.activeClaimsCount ASC LIMIT 1
    """)
    Optional<Agent> findBestAvailableAgent(@Param("type") String claimType);

    @Query("SELECT ag from Agent ag WHERE ag.available = true ORDER BY ag.activeClaimsCount ASC LIMIT 1")
    Optional<Agent> findLeastBusyAgent();


}
