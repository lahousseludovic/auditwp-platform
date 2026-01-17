package com.auditwp.platform.controller;

import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.dto.AuditScores;
import com.auditwp.platform.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping
    public AuditScores startAudit(@RequestBody AuditRequest request) {
        return this.auditService.runAudit(request);
    }
}
