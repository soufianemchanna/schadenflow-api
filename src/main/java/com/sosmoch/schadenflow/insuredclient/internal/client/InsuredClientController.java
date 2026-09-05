package com.sosmoch.schadenflow.insuredclient.internal.client;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insured-client")
@Tag(name = "Client", description = "Insured client operations")
@RequiredArgsConstructor
public class InsuredClientController {
}
