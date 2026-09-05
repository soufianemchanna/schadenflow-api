package com.sosmoch.schadenflow.auth;


import com.sosmoch.schadenflow.auth.dto.AuthResponse;
import com.sosmoch.schadenflow.auth.dto.LoginRequest;
import com.sosmoch.schadenflow.auth.dto.RegisterRequest;
import com.sosmoch.schadenflow.core.identity.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth APIs: Register, login, and current user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok().body(
                authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(@AuthenticationPrincipal UserPrincipal currentUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.me(currentUser));
    }
}
