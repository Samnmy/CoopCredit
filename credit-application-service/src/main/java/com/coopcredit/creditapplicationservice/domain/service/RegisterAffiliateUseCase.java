package com.coopcredit.creditapplicationservice.domain.service;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.domain.model.AffiliateStatus;
import com.coopcredit.creditapplicationservice.domain.port.AffiliatePort;
import org.springframework.stereotype.Service;

@Service
public class RegisterAffiliateUseCase {

    private final AffiliatePort affiliatePort;

    public RegisterAffiliateUseCase(AffiliatePort affiliatePort) {
        this.affiliatePort = affiliatePort;
    }

    public Affiliate execute(Affiliate affiliate) {
        // Validaciones de negocio
        if (affiliate.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be greater than 0");
        }
        if (affiliatePort.existsByDocument(affiliate.getDocument())) {
            throw new IllegalArgumentException("Document already exists");
        }
        if (affiliate.getStatus() == null) {
            affiliate.setStatus(AffiliateStatus.ACTIVE);
        }
        return affiliatePort.save(affiliate);
    }
}