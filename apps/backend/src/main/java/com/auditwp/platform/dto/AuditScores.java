package com.auditwp.platform.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AuditScores {

    private String url;
    private Map<String, Double> scores;
}
