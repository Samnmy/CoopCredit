// CreditApplicationResponse.java
package com.coopcredit.creditapplicationservice.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreditApplicationResponse {
    private Long id;
    private AffiliateResponse affiliate;
    private BigDecimal requestedAmount;
    private Integer termMonths;
    private Double proposedRate;
    private LocalDate applicationDate;
    private String status;
    private RiskEvaluationResponse riskEvaluation;
    private BigDecimal monthlyPayment;
}