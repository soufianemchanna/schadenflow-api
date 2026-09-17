package com.sosmoch.schadenflow.user;

public record CreateUserCommand(
        String email,
        String password,
        Role role
) {
}
