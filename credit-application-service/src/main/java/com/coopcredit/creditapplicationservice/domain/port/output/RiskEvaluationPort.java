package com.coopcredit.creditapplicationservice.domain.port.output;

import com.coopcredit.creditapplicationservice.domain.model.RiskEvaluation;

public interface RiskEvaluationPort {
    RiskEvaluation evaluateRisk(String documentNumber, Double requestedAmount,
                                Integer termMonths, Double salary);
}