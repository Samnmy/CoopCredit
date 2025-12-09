// CreditApplicationMapper.java
package com.coopcredit.creditapplicationservice.application.mapper;

import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import com.coopcredit.creditapplicationservice.application.dto.CreditApplicationRequest;
import com.coopcredit.creditapplicationservice.application.dto.CreditApplicationResponse;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.entity.CreditApplicationEntity;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring",
        uses = {AffiliateMapper.class, RiskEvaluationMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreditApplicationMapper {

    @Mapping(source = "affiliateId", target = "affiliate.id")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "applicationDate", ignore = true)
    @Mapping(target = "riskEvaluation", ignore = true)
    CreditApplication toDomain(CreditApplicationRequest request);

    @Mapping(source = "affiliate", target = "affiliate")
    @Mapping(source = "riskEvaluation", target = "riskEvaluation")
    CreditApplicationResponse toResponse(CreditApplication domain);

    @Mapping(source = "affiliate", target = "affiliate")
    @Mapping(source = "riskEvaluation", target = "riskEvaluation")
    CreditApplication toDomain(CreditApplicationEntity entity);

    @Mapping(source = "affiliate", target = "affiliate")
    @Mapping(source = "riskEvaluation", target = "riskEvaluation")
    CreditApplicationEntity toEntity(CreditApplication domain);

    @AfterMapping
    default void calculateMonthlyPayment(@MappingTarget CreditApplicationResponse response,
                                         CreditApplication domain) {
        if (domain.getRequestedAmount() != null &&
                domain.getProposedRate() != null &&
                domain.getTermMonths() != null) {
            double amount = domain.getRequestedAmount().doubleValue();
            double annualRate = domain.getProposedRate();
            int months = domain.getTermMonths();

            double monthlyRate = annualRate / 100 / 12;
            double monthlyPayment;

            if (monthlyRate == 0) {
                monthlyPayment = amount / months;
            } else {
                monthlyPayment = (amount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                        (Math.pow(1 + monthlyRate, months) - 1);
            }

            response.setMonthlyPayment(BigDecimal.valueOf(monthlyPayment)
                    .setScale(2, RoundingMode.HALF_UP));
        }
    }
}
