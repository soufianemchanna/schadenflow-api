package com.sosmoch.schadenflow.auth;


import com.sosmoch.schadenflow.auth.dto.AuthResponse;
import com.sosmoch.schadenflow.auth.dto.LoginRequest;
import com.sosmoch.schadenflow.auth.dto.RegisterRequest;
import com.sosmoch.schadenflow.auth.internal.JwtService;
import com.sosmoch.schadenflow.insuredclient.InsuredClientRepository;
import com.sosmoch.schadenflow.insuredclient.InsuredClientStatus;
import com.sosmoch.schadenflow.user.Role;
import com.sosmoch.schadenflow.user.User;
import com.sosmoch.schadenflow.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final InsuredClientRepository insuredClientRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request){
        // 1/ identity check client number / email
        log.info("Registering client with client number: {}", request.clientNumber());
        log.info("Registering client with email: {}", request.email());
        var insuredClient = insuredClientRepository.findInsuredClientByClientNumberAndEmail(
                request.clientNumber(), request.email()
        ).orElseThrow(()-> new IdentityVerificationException(
                "We could not verify your identity. Please contact your insurer."
        ));

        // 2/ check already has an account
        if(insuredClient.getUserId() != null){
            throw new IdentityVerificationException(
                    "An account already exists for this client number."
            );
        }

        // 3/ create user
        var user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setUserRole(Role.CLIENT);
        var savedUser = userRepository.save(user);

        // 4/ link user to insured client
        insuredClient.setUserId(savedUser.getId());
        insuredClient.setStatus(InsuredClientStatus.ACTIVE);
        insuredClient.setRegisteredAt(LocalDateTime.now());
        insuredClientRepository.save(insuredClient);

        return new AuthResponse(
                jwtService.generateToken(savedUser),
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUserRole()
        );
    }

    public AuthResponse login(LoginRequest request){
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        if(!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new BadCredentialsException("Invalid email or password.");
        }

        return new AuthResponse(
                jwtService.generateToken(user),
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    public AuthResponse me(User currentUser) {
        return new AuthResponse(
                null,
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getUserRole()
        );
    }
}
