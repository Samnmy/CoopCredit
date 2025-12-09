package com.coopcredit.creditapplicationservice.infrastructure.rest.client;

import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RiskEvaluationRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RiskEvaluationResponse;
import org.springframework.stereotype.Component;

@Component
public class RiskCentralClientFallback implements RiskCentralClient {

    @Override
    public RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request) {
        // Respuesta por defecto si el servicio está caído
        RiskEvaluationResponse response = new RiskEvaluationResponse();
        response.setScore(50);  // Score neutral
        response.setRiskLevel("MEDIUM");
        response.setDetails("Risk Central Service temporarily unavailable. Using default evaluation.");
        response.setApproved(true);  // Por defecto aprobado
        response.setSuggestedRate(12.5);  // Tasa sugerida por defecto
        return response;
    }
}