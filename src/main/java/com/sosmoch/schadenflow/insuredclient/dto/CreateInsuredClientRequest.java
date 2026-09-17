package com.sosmoch.schadenflow.insuredclient.dto;

import com.sosmoch.schadenflow.insuredclient.enums.Gender;
import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreateInsuredClientRequest(
        @NotBlank String clientNumber,
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank @Past LocalDate dateOfBirth,
        @NotBlank Gender gender,
        InsuredClientStatus status
) {
}
