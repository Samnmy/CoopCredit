// RiskCentralClientErrorDecoder.java
package com.coopcredit.creditapplicationservice.infrastructure.rest.client;

import feign.Response;
import feign.codec.ErrorDecoder;

public class RiskCentralClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        // Implementar manejo de errores específicos
        return new RuntimeException("Error calling risk central service");
    }
}