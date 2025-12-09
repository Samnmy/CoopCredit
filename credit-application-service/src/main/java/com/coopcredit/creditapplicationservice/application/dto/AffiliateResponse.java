package com.coopcredit.creditapplicationservice.application.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AffiliateResponse {
    private Long id;
    private String document;
    private String name;
    private Double salary;
    private LocalDate affiliationDate;
    private String status;
    private Integer totalApplications;
}