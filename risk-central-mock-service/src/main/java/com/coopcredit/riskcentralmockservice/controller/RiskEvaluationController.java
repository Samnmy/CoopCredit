// RiskEvaluationController.java
package com.coopcredit.riskcentralmockservice.controller;

import com.coopcredit.riskcentralmockservice.dto.RiskEvaluationRequest;
import com.coopcredit.riskcentralmockservice.dto.RiskEvaluationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class RiskEvaluationController {

    @PostMapping("/risk-evaluation")
    public RiskEvaluationResponse evaluate(@RequestBody RiskEvaluationRequest request) {
        log.info("Risk evaluation request for document: {}", request.getDocumento());

        // Convertir documento a seed numérico
        int seed = Math.abs(request.getDocumento().hashCode()) % 1000;

        // Generar score entre 300 y 950 basado en seed
        int score = 300 + (seed % 651); // 950-300+1 = 651

        String nivelRiesgo;
        String detalle;

        if (score <= 500) {
            nivelRiesgo = "HIGH";
            detalle = "High credit risk. Significant payment delays in history.";
        } else if (score <= 700) {
            nivelRiesgo = "MEDIUM";
            detalle = "Moderate credit risk. Some payment delays observed.";
        } else {
            nivelRiesgo = "LOW";
            detalle = "Low credit risk. Good payment history.";
        }

        // Ajustar según monto y plazo
        if (request.getMonto() > 10000000) {
            score -= 50;
            detalle += " High amount requested.";
        }

        if (request.getPlazo() > 60) {
            score -= 30;
            detalle += " Long term requested.";
        }

        // Asegurar que el score esté en rango
        score = Math.max(300, Math.min(950, score));

        RiskEvaluationResponse response = new RiskEvaluationResponse();
        response.setDocumento(request.getDocumento());
        response.setScore(score);
        response.setNivelRiesgo(nivelRiesgo);
        response.setDetalle(detalle);

        log.info("Risk evaluation response - Score: {}, Level: {}", score, nivelRiesgo);
        return response;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}