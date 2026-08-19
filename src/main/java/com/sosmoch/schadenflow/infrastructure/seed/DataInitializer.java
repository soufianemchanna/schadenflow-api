package com.sosmoch.schadenflow.infrastructure.seed;

import com.sosmoch.schadenflow.user.Role;
import com.sosmoch.schadenflow.user.User;
import com.sosmoch.schadenflow.user.UserRepository;
import com.sosmoch.schadenflow.claim.ProductType;
import com.sosmoch.schadenflow.insuredclient.Gender;
import com.sosmoch.schadenflow.insuredclient.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.InsuredClientRepository;
import com.sosmoch.schadenflow.insuredclient.InsuredClientStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final InsuredClientRepository insuredClientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(insuredClientRepository.count() > 0){
            log.info("InsuredClient table is not empty, skipping");
            return;
        }
        log.info("Seeding dev data...");
        
        seedAdminUser();
        seedInsuredClients();

        log.info("Dev data seeded");
    }


    // --- SEED Insured Clients DATA ---
    private void seedInsuredClients() {
        // Client 1 — PENDING, no account yet
        var client1 = new InsuredClient();
        client1.setClientNumber("AT-2024-00441");
        client1.setFullName("Anna Müller");
        client1.setEmail("anna.mueller@email.at");
        client1.setDateOfBirth(LocalDate.of(1990, 5, 14));
        client1.setProductType(ProductType.AUTO);
        client1.setStartDate(LocalDate.of(2026, 11, 1));
        client1.setStatus(InsuredClientStatus.PENDING_REGISTRATION);
        client1.setGender(Gender.FEMALE);

        // Client 2 — PENDING
        var client2 = new InsuredClient();
        client2.setClientNumber("AT-2024-00442");
        client2.setFullName("Thomas Berger");
        client2.setEmail("thomas.berger@email.at");
        client2.setDateOfBirth(LocalDate.of(1985, 11, 3));
        client2.setProductType(ProductType.HOME);
        client2.setStartDate(LocalDate.of(2026, 10, 15));
        client2.setStatus(InsuredClientStatus.PENDING_REGISTRATION);
        client2.setGender(Gender.MALE);

        // Client 3 — ACTIVE, already has an account
        var client3 = new InsuredClient();
        client3.setClientNumber("AT-2024-00443");
        client3.setFullName("Sophie Wagner");
        client3.setEmail("sophie.wagner@email.at");
        client3.setDateOfBirth(LocalDate.of(1995, 7, 22));
        client3.setProductType(ProductType.HEALTH);
        client3.setStartDate(LocalDate.of(2026, 10, 1));
        client3.setStatus(InsuredClientStatus.ACTIVE);
        client3.setGender(Gender.FEMALE);

        insuredClientRepository.saveAll(List.of(client1, client2, client3));

        // Create the portal User for client3 and link them
        var user3 = new User();
        user3.setEmail("sophie.wagner@email.at");
        user3.setPasswordHash(passwordEncoder.encode("client123"));
        user3.setUserRole(Role.CLIENT);
        var savedUser3 = userRepository.save(user3);

        client3.setUserId(savedUser3.getId());
        insuredClientRepository.save(client3);

        log.info("[Seed] 3 InsuredClients created");
        log.info("[Seed] Pending → anna.mueller@email.at");
        log.info("[Seed] Pending → thomas.berger@email.at");
        log.info("[Seed] Active  → sophie.wagner@email.at (already registered)");
    }

    // --- SEED ADMIN ACCOUNT ---
    private void seedAdminUser() {
        var admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setPasswordHash(passwordEncoder.encode("lool"));
        admin.setUserRole(Role.ADMIN);
        userRepository.save(admin);
        log.info("Admin user created");
    }
}
