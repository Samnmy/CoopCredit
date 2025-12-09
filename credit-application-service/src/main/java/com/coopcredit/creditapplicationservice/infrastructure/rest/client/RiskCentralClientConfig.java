// RiskCentralClientConfig.java
package com.coopcredit.creditapplicationservice.infrastructure.rest.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RiskCentralClientConfig {
    @Bean
    public RiskCentralClientErrorDecoder riskCentralClientErrorDecoder() {
        return new RiskCentralClientErrorDecoder();
    }
}