package com.auditwp.platform.service;

import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.dto.AuditScores;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuditNodeClient {

    private final WebClient webClient;

    public AuditNodeClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:3001") // ton service Node
                .build();
    }

    public AuditScores launchAudit(final AuditRequest auditRequest) {

        return webClient.post()
                .uri("/audit")
                .bodyValue(auditRequest)
                .retrieve()
                .bodyToMono(AuditScores.class)
                .block(); // pour simplifier, blocage ici (synchrone)
    }
}
