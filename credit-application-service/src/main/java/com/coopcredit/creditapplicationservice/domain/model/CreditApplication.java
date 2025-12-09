package com.coopcredit.creditapplicationservice.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplication {
    private Long id;
    private Affiliate affiliate;
    private BigDecimal requestedAmount;
    private Integer termMonths;
    private Double proposedRate;
    private LocalDate applicationDate;
    private ApplicationStatus status;
    private RiskEvaluation riskEvaluation;
}