package com.sosmoch.schadenflow.auth.internal;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthEntryPoint customAuthEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/actuator/health"
                                ).permitAll()

                                // single role based
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/client/**").hasRole("CLIENT")
                                .requestMatchers("/api/agent/**").hasRole("AGENT")
                                // shared roles
                                .requestMatchers("/api/claims/**").hasAnyRole("ADMIN", "CLIENT", "AGENT")
                                .requestMatchers("/api/notification/**").hasAnyRole( "CLIENT", "AGENT")
                                // at least authenticated
                                .anyRequest().authenticated()
                        )
                // exception handling
                .exceptionHandling(ex -> ex
                                .accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthEntryPoint)
                        )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
