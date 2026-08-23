package com.sosmoch.schadenflow;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ModularityTests {
    ApplicationModules appModules = ApplicationModules.of(SchadenflowApplication.class);

    @Test
    void verifyModules() {
        appModules.verify();
    }

    @Test
    void writeDocumentation(){
        new Documenter(appModules).writeDocumentation().writeIndividualModulesAsPlantUml();
    }
}
