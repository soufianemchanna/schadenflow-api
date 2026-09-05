package com.sosmoch.schadenflow.infrastructure.seed;

import com.sosmoch.schadenflow.auth.AuthFacade;
import com.sosmoch.schadenflow.insuredclient.InsuredClientFacade;
import com.sosmoch.schadenflow.insuredclient.dto.CreateInsuredClientRequest;
import com.sosmoch.schadenflow.user.CreateUserCommand;
import com.sosmoch.schadenflow.user.Role;
import com.sosmoch.schadenflow.user.UserFacade;
import com.sosmoch.schadenflow.insuredclient.enums.Gender;
import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;
import com.sosmoch.schadenflow.user.UserView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final InsuredClientFacade insuredClientFacade;
    private final UserFacade userFacade;
    private final AuthFacade authFacade;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(insuredClientFacade.countInsuredClients() > 0){
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
        CreateInsuredClientRequest client1 = new CreateInsuredClientRequest(
            "AT-2024-00441",
            "Anna Müller",
            "anna.mueller@email.at",
                LocalDate.of(1990, 5, 14),
            Gender.FEMALE,
            InsuredClientStatus.PENDING_REGISTRATION
        );

        // Client 2 — PENDING
        CreateInsuredClientRequest client2 = new CreateInsuredClientRequest(
            "AT-2024-00442",
            "Thomas Berger",
            "thomas.berger@email.at",
            LocalDate.of(1985, 11, 3),
            Gender.MALE,
            InsuredClientStatus.PENDING_REGISTRATION
        );

        // Client 3 — ACTIVE already has an account
        CreateInsuredClientRequest client3 = new CreateInsuredClientRequest(
            "AT-2024-00443",
            "Sophie Wagner",
            "sophie.wagner@email.at",
            LocalDate.of(1995, 7, 22),
            Gender.FEMALE,
            InsuredClientStatus.ACTIVE
        );

        insuredClientFacade.createInsuredClients(List.of(client1, client2, client3));

        // Create the portal User for client3 and link them
        CreateUserCommand user3 = new CreateUserCommand(
                "sophie.wagner@email.at",
                authFacade.passwordEncoder().encode("sophie123"),
                Role.CLIENT
        );

        UserView userView = userFacade.createUser(user3);

        insuredClientFacade.linkUserAccount(userView.id(), "AT-2024-00443");

        log.info("[Seed] 3 InsuredClients created");
        log.info("[Seed] Pending → anna.mueller@email.at");
        log.info("[Seed] Pending → thomas.berger@email.at");
        log.info("[Seed] Active  → sophie.wagner@email.at (already registered)");
    }

    // --- SEED ADMIN ACCOUNT ---
    private void seedAdminUser() {
        CreateUserCommand admin = new CreateUserCommand(
                "admin@gmail.com",
                authFacade.passwordEncoder().encode("admin123"),
                Role.ADMIN
        );
        userFacade.createUser(admin);
        log.info("Admin user created");
    }
}
