// AffiliateRequest.java
package com.coopcredit.creditapplicationservice.application.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class AffiliateRequest {
    @NotBlank(message = "Document is required")
    private String document;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than 0")
    @DecimalMin(value = "100.0", message = "Salary must be at least 100.0")
    private Double salary;

    @NotBlank(message = "Affiliation date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format yyyy-MM-dd")
    private String affiliationDate;

    private String status = "ACTIVE";
}