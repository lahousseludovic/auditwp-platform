package com.auditwp.platform.controller;

import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.dto.AuditResult;
import com.auditwp.platform.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping
    public ResponseEntity<AuditResult> launchAudit(@RequestBody AuditRequest request) {
        final AuditResult result = auditService.runAudit(request);
        // Code HTTP 200 si success, 400 si erreur de validation Node
        return ResponseEntity
                .status(result.isSuccess() ? 200 : 400)
                .body(result);
    }
}
