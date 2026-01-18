package com.auditwp.platform.service;

import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.dto.AuditResult;
import com.auditwp.platform.dto.AuditScores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditNodeClient auditNodeClient;

    public AuditResult runAudit(final AuditRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return new AuditResult(false, "Adresse email manquante", null);
        }

        if (request.getUrl() == null || request.getUrl().isBlank()) {
            return new AuditResult(false, "Url à auditer manquante", null);
        }

        AuditScores scores;
        try {
            // Appel node
            scores = this.auditNodeClient.launchAudit(request);
        } catch (Exception e) {
            return new AuditResult(false, "Impossible de lancer l'audit : " + e.getMessage(), null);
        }

        // Audit OK
        return new AuditResult(true, "Audit réalisé avec succès", scores.getUrl());
    }
}
