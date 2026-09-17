package com.sosmoch.schadenflow.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface AuthFacade {
    PasswordEncoder passwordEncoder();
}
