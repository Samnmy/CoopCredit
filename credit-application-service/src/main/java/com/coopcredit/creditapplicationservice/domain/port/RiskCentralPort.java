package com.coopcredit.creditapplicationservice.domain.port;

import com.coopcredit.creditapplicationservice.domain.model.RiskEvaluation;

public interface RiskCentralPort {
    RiskEvaluation evaluateRisk(String documentNumber, Double requestedAmount, Integer termMonths);
}