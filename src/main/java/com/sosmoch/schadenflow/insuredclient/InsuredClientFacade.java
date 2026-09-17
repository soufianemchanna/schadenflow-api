package com.sosmoch.schadenflow.insuredclient;

import com.sosmoch.schadenflow.insuredclient.dto.CreateInsuredClientRequest;
import com.sosmoch.schadenflow.insuredclient.dto.InsuredClientView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsuredClientFacade {

    Optional<InsuredClientView> verifyClientHasIdentity(String clientNumber, String email);
    void activateClientAccount(UUID userId, UUID insuredClientId);

    void createInsuredClients(List<CreateInsuredClientRequest> clients);
    long countInsuredClients();
    void linkUserAccount(UUID userId, String clientNumber);


}
