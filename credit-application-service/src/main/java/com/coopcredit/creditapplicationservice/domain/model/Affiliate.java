package com.coopcredit.creditapplicationservice.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Affiliate {
    private Long id;
    private String document;
    private String name;
    private Double salary;
    private LocalDate affiliationDate;
    private AffiliateStatus status;

    @Builder.Default
    private List<CreditApplication> creditApplications = new ArrayList<>();
}