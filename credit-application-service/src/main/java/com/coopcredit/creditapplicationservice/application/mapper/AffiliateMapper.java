// AffiliateMapper.java
package com.coopcredit.creditapplicationservice.application.mapper;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.application.dto.AffiliateRequest;
import com.coopcredit.creditapplicationservice.application.dto.AffiliateResponse;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.entity.AffiliateEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AffiliateMapper {

    @Mapping(source = "affiliationDate", target = "affiliationDate", dateFormat = "yyyy-MM-dd")
    Affiliate toDomain(AffiliateRequest request);

    AffiliateResponse toResponse(Affiliate domain);

    @Mapping(target = "creditApplications", ignore = true)
    Affiliate toDomain(AffiliateEntity entity);

    @Mapping(target = "creditApplications", ignore = true)
    AffiliateEntity toEntity(Affiliate domain);

    @AfterMapping
    default void calculateTotalApplications(@MappingTarget AffiliateResponse response,
                                            Affiliate domain) {
        if (domain.getCreditApplications() != null) {
            response.setTotalApplications(domain.getCreditApplications().size());
        }
    }
}