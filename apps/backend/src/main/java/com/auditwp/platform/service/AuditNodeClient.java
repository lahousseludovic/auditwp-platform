package com.auditwp.platform.service;

import com.auditwp.platform.BackendApplicationProperties;
import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.dto.AuditScores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuditNodeClient {

    private final WebClient webClient;

    public AuditNodeClient(final WebClient.Builder builder, final BackendApplicationProperties properties) {
        this.webClient = builder
                .baseUrl(properties.getAuditBaseUrl()) // ton service Node
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
