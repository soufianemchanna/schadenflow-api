package com.sosmoch.schadenflow.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest (
        @NotBlank
        String clientNumber,

        @NotBlank
        String email,

        @NotBlank
        String password
)
{
}
