package com.auditwp.platform.controller;

import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping
    public ResponseEntity<String> launchAudit(@RequestBody AuditRequest request) {
        try {
            auditService.runAudit(request); // pas besoin de récupérer de données
            return ResponseEntity.ok("L'audit a été effectué avec succès. Le rapport a été envoyé par email");
        } catch (RuntimeException e) {
            log.error("Erreur lors du lancement de l'audit", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du lancement de l'audit");
        }
    }
}
