package com.sosmoch.schadenflow.user;

import com.sosmoch.schadenflow.user.internal.User;

import java.util.UUID;

public record UserView(
        UUID id,
        String email,
        Role role,
        String password, // only for login check purposes, by default is null
        boolean enabled
) {
    public static UserView from(User user) {
        return new UserView(user.getId(), user.getEmail(), user.getUserRole(), null, user.isEnabled());
    }
}
