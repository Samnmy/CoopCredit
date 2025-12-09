package com.coopcredit.creditapplicationservice.infrastructure.rest.adapter;

import com.coopcredit.creditapplicationservice.domain.model.RiskEvaluation;
import com.coopcredit.creditapplicationservice.domain.model.RiskLevel;
import com.coopcredit.creditapplicationservice.domain.port.RiskCentralPort;
import com.coopcredit.creditapplicationservice.infrastructure.rest.client.RiskCentralClient;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RiskEvaluationRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RiskEvaluationResponse;
import org.springframework.stereotype.Component;

@Component
public class RiskCentralAdapter implements RiskCentralPort {

    private final RiskCentralClient riskCentralClient;

    public RiskCentralAdapter(RiskCentralClient riskCentralClient) {
        this.riskCentralClient = riskCentralClient;
    }

    @Override
    public RiskEvaluation evaluateRisk(String documentNumber, Double requestedAmount, Integer termMonths) {
        // Si necesitas salario, puedes obtenerlo de otro servicio
        Double salary = 3000.0; // Valor por defecto o obtener de un servicio

        RiskEvaluationRequest request = new RiskEvaluationRequest();
        request.setDocumentNumber(documentNumber);
        request.setRequestedAmount(requestedAmount);
        request.setTermMonths(termMonths);
        request.setSalary(salary);

        RiskEvaluationResponse response = riskCentralClient.evaluateRisk(request);

        RiskEvaluation evaluation = new RiskEvaluation();
        evaluation.setScore(response.getScore());

        // Convertir String a enum RiskLevel
        try {
            RiskLevel riskLevel = RiskLevel.valueOf(response.getRiskLevel().toUpperCase());
            evaluation.setRiskLevel(riskLevel);
        } catch (IllegalArgumentException e) {
            evaluation.setRiskLevel(RiskLevel.MEDIUM); // Valor por defecto
        }

        evaluation.setDetails(response.getDetails());
        evaluation.setApproved(response.getApproved());

        return evaluation;
    }
}