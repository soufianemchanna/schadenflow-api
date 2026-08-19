package com.sosmoch.schadenflow.auth.dto;

import com.sosmoch.schadenflow.user.Role;

import java.util.UUID;

public record AuthResponse (
    String token,
    UUID userId,
    String email,
    Role role
)
{
}
