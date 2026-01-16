package com.auditwp.platform.controller;

import com.auditwp.platform.dto.AuditRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
public class Audit {

    // Endpoint POST pour lancer un audit
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> runAudit(@RequestBody AuditRequest request) {

        Map<String, Object> response = new HashMap<>();

        // Vérification simple des paramètres
        if (request.getEmail().isEmpty() || request.getUrl().isEmpty()) {
            response.put("success", false);
            response.put("message", "Email et URL sont obligatoires !");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Pour le MVP, on retourne juste les données reçues
        response.put("success", true);
        response.put("email", request.getEmail());
        response.put("url", request.getUrl());
        response.put("message", "Audit reçu, traitement à implémenter.");

        // Ici plus tard tu pourrais appeler ton service Node pour lancer l'audit

        return ResponseEntity.ok(response);
    }
}
