// CreditApplicationRequest.java
package com.coopcredit.creditapplicationservice.application.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class CreditApplicationRequest {
    @NotNull(message = "Affiliate ID is required")
    private Long affiliateId;

    @NotNull(message = "Requested amount is required")
    @DecimalMin(value = "1000.0", message = "Requested amount must be at least 1000")
    @DecimalMax(value = "10000000.0", message = "Requested amount cannot exceed 10,000,000")
    private BigDecimal requestedAmount;

    @NotNull(message = "Term months is required")
    @Min(value = 3, message = "Term months must be at least 3")
    @Max(value = 360, message = "Term months cannot exceed 360")
    private Integer termMonths;

    @NotNull(message = "Proposed rate is required")
    @DecimalMin(value = "0.0", message = "Proposed rate must be 0 or positive")
    @DecimalMax(value = "30.0", message = "Proposed rate cannot exceed 30")
    private Double proposedRate;
}