package com.auditwp.platform.service;

import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.dto.AuditResult;
import com.auditwp.platform.dto.AuditScores;
import com.auditwp.platform.dto.AuditStatus;
import com.auditwp.platform.entity.AuditJobEntity;
import com.auditwp.platform.repository.AuditJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditNodeClient auditNodeClient;
    @Autowired
    private MailService mailService;
    @Autowired
    private AuditJobRepository repository;

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

            if (scores != null) {

                String html = "<!DOCTYPE html>" +
                        "<html lang='fr'>" +
                        "<head>" +
                        "<meta charset='UTF-8' />" + // <-- note le "/" à la fin
                        "<title>Rapport Audit</title>" +
                        "</head>" +
                        "<body>" +
                        "<h1>Rapport Audit</h1>" +
                        "<p>URL : " + request.getUrl() + "</p>" +
                        "<p>Score SEO : " + scores.getScores().getSeo() + "</p>" +
                        "<p>Score Performance : " + scores.getScores().getPerformance() + "</p>" +
                                "</body>" +
                                        "</html>";


                byte[] pdf = PdfGeneratorService.generatePdfFromHtml(html);

                mailService.sendMailWithPdf(
                        request.getEmail(),
                        "Votre rapport AuditWP",
                        "Bonjour,\nVeuillez trouver ci-joint le rapport PDF de votre audit.",
                        pdf,
                        "audit-report.pdf"
                );

                repository.save(AuditJobEntity.builder()
                        .url(request.getUrl())
                        .email(request.getEmail())
                        .status(AuditStatus.SUCCESS)
                        .errorMessage("")
                        .build());

            }

        } catch (Exception e) {
            repository.save(AuditJobEntity.builder()
                    .url(request.getUrl())
                    .email(request.getEmail())
                    .status(AuditStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .build());
            return new AuditResult(false, "Impossible de lancer l'audit : " + e.getMessage(), null);
        }

        // Audit OK
        return new AuditResult(true, "Audit réalisé avec succès", scores.getUrl());
    }
}
