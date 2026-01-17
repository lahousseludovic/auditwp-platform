package com.auditwp.platform.service;

import com.auditwp.platform.dto.AuditRequest;
import com.auditwp.platform.dto.AuditScores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditNodeClient auditNodeClient;

    public AuditScores runAudit(final AuditRequest request) {
        return this.auditNodeClient.launchAudit(request);
    }
}
