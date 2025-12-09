package com.coopcredit.creditapplicationservice.domain.model;

import lombok.Data;

@Data
public class RiskEvaluation {
    private Integer score;
    private RiskLevel riskLevel;
    private String details;
    private Boolean approved;

    // Constructor sin argumentos
    public RiskEvaluation() {
    }

    // Constructor con argumentos
    public RiskEvaluation(Integer score, RiskLevel riskLevel, String details, Boolean approved) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.details = details;
        this.approved = approved;
    }
}