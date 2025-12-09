package com.coopcredit.creditapplicationservice.infrastructure.rest.client;

import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RiskEvaluationRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RiskEvaluationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "risk-central-service",
        url = "${risk-central.service.url}",  // Configurable desde application.yml
        fallback = RiskCentralClientFallback.class  // Opcional: para resiliencia
)
public interface RiskCentralClient {

    @PostMapping("/api/risk-evaluations")
    RiskEvaluationResponse evaluateRisk(@RequestBody RiskEvaluationRequest request);
}