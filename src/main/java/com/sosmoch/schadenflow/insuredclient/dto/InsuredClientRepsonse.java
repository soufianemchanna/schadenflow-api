package com.sosmoch.schadenflow.insuredclient.dto;

import com.sosmoch.schadenflow.insuredclient.enums.Gender;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record InsuredClientRepsonse(
        UUID id,
        String fullName,
        String clientNumber,
        String email,
        LocalDate dateOfBirth,
        Gender gender,
        InsuredClientStatus status,
        UUID userId,
        LocalDateTime registeredAt

) {
    public static InsuredClientRepsonse from(InsuredClient ic) {
        return new InsuredClientRepsonse(
                ic.getId(),
                ic.getFullName(),
                ic.getClientNumber(),
                ic.getEmail(),
                ic.getDateOfBirth(),
                ic.getGender(),
                ic.getStatus(),
                ic.getUserId(),
                ic.getRegisteredAt()
        );
    }
}
