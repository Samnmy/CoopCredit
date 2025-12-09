package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import lombok.Data;

@Data  // Si usas Lombok, esto genera getters y setters automáticamente
public class RiskEvaluationRequest {
    private String documentNumber;  // ← El campo probablemente se llama así
    private Double requestedAmount;
    private Integer termMonths;
    private Double salary;

    // Con Lombok @Data, se generan automáticamente:
    // getDocumentNumber(), setDocumentNumber(String documentNumber)
    // getRequestedAmount(), setRequestedAmount(Double requestedAmount)
    // etc.
}