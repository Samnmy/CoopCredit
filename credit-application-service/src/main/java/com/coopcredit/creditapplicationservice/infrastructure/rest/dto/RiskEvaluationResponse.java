package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import lombok.Data;

@Data
public class RiskEvaluationResponse {
    private Integer score;
    private String riskLevel;  // HIGH, MEDIUM, LOW
    private String details;
    private Boolean approved;
    private Double suggestedRate;
}