package com.coopcredit.riskcentralmockservice.dto;

import lombok.Data;

@Data
public class RiskEvaluationResponse {
    private String documento;
    private Integer score;
    private String nivelRiesgo;
    private String detalle;
}