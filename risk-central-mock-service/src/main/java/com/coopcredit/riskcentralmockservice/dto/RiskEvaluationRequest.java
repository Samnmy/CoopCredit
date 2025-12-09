package com.coopcredit.riskcentralmockservice.dto;

import lombok.Data;

@Data
public class RiskEvaluationRequest {
    private String documento;
    private Double monto;
    private Integer plazo;
}