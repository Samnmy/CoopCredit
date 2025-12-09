package com.coopcredit.creditapplicationservice.domain.port;

import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import com.coopcredit.creditapplicationservice.domain.model.ApplicationStatus;
import java.util.List;
import java.util.Optional;

public interface CreditApplicationPort {
    CreditApplication save(CreditApplication creditApplication);
    Optional<CreditApplication> findById(Long id);
    List<CreditApplication> findAll();
    List<CreditApplication> findByAffiliateId(Long affiliateId);
    List<CreditApplication> findByStatus(ApplicationStatus status);
    void deleteById(Long id);
}