// RiskEvaluationResponse.java
package com.coopcredit.creditapplicationservice.application.dto;

import lombok.Data;

@Data
public class RiskEvaluationResponse {
    private Integer score;
    private String riskLevel;
    private String details;
}