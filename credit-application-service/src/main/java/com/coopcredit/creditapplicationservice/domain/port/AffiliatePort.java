package com.coopcredit.creditapplicationservice.domain.port;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import java.util.List;
import java.util.Optional;

public interface AffiliatePort {
    Affiliate save(Affiliate affiliate);
    Optional<Affiliate> findById(Long id);
    Optional<Affiliate> findByDocument(String document);
    List<Affiliate> findAll();
    void deleteById(Long id);
    boolean existsByDocument(String document);
}