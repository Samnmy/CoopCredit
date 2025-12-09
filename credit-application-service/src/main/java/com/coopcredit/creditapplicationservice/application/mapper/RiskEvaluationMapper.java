package com.coopcredit.creditapplicationservice.application.mapper;

import com.coopcredit.creditapplicationservice.domain.model.RiskEvaluation;
import com.coopcredit.creditapplicationservice.application.dto.RiskEvaluationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskEvaluationMapper {
    RiskEvaluationResponse toResponse(RiskEvaluation riskEvaluation);
}