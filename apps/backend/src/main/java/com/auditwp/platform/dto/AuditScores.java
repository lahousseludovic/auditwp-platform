package com.auditwp.platform.dto;

import lombok.Data;
import java.util.Map;

@Data
public class AuditScores {

    /**
     * URL finale auditée (après redirections éventuelles)
     */
    private String url;

    /**
     * Scores globaux Lighthouse (0 → 100)
     */
    private Scores scores;

    /**
     * Web Vitals et métriques clés
     */
    private Vitals vitals;
}
