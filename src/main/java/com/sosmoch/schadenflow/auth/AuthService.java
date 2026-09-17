package com.sosmoch.schadenflow.auth;


import com.sosmoch.schadenflow.auth.dto.AuthResponse;
import com.sosmoch.schadenflow.auth.dto.LoginRequest;
import com.sosmoch.schadenflow.auth.dto.RegisterRequest;
import com.sosmoch.schadenflow.auth.internal.JwtService;
import com.sosmoch.schadenflow.core.identity.UserPrincipal;
import com.sosmoch.schadenflow.insuredclient.InsuredClientFacade;
import com.sosmoch.schadenflow.insuredclient.dto.InsuredClientView;
import com.sosmoch.schadenflow.user.CreateUserCommand;
import com.sosmoch.schadenflow.user.Role;
import com.sosmoch.schadenflow.user.UserFacade;
import com.sosmoch.schadenflow.user.UserView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthFacade{
    private final UserFacade userFacade;
    private final InsuredClientFacade insuredClientFacade;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request){
        // 1/ identity check client number / email
        log.info("Registering client with client number: {}", request.clientNumber());
        log.info("Registering client with email: {}", request.email());
        Optional<InsuredClientView> clientView = insuredClientFacade.verifyClientHasIdentity(
                request.clientNumber(), request.email()
        );

        if(clientView.isEmpty()){
            throw new IdentityVerificationException(
                    "Client number or email is not registered."
            );
        }

        // 2/ check already has an account
        if(clientView.get().userId() != null){
            throw new IdentityVerificationException(
                    "An account already exists for this client number."
            );
        }

        // 3/ create user
        CreateUserCommand user = new CreateUserCommand(
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.CLIENT
        );

        UserView savedUser = userFacade.createUser(user);

        // 4/ link user to insured client
        insuredClientFacade.activateClientAccount(savedUser.id(), clientView.get().id());

        return new AuthResponse(
                jwtService.generateToken(Optional.of(savedUser)),
                savedUser.id(),
                savedUser.email(),
                savedUser.role()
        );
    }

    public AuthResponse login(LoginRequest request){
        Optional<UserView> user = userFacade.findUserByEmail(request.email());

        if (user.isEmpty()){
            throw  new BadCredentialsException("Invalid email or password.");
        }

        if(!passwordEncoder.matches(request.password(), user.get().password())){
            throw new BadCredentialsException("Invalid email or password.");
        }

        return new AuthResponse(
                jwtService.generateToken(user),
                user.get().id(),
                user.get().email(),
                user.get().role()
        );
    }

    public AuthResponse me(UserPrincipal currentUser) {
        return new AuthResponse(
                null,
                currentUser.id(),
                currentUser.email(),
                Role.valueOf(currentUser.role())
        );
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
