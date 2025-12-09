package com.coopcredit.creditapplicationservice.infrastructure.persistence.adapter;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.domain.port.AffiliatePort;
import com.coopcredit.creditapplicationservice.application.mapper.AffiliateMapper;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.repository.AffiliateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AffiliateAdapter implements AffiliatePort {

    private final AffiliateRepository affiliateRepository;
    private final AffiliateMapper affiliateMapper;

    @Override
    public Affiliate save(Affiliate affiliate) {
        var entity = affiliateMapper.toEntity(affiliate);
        var savedEntity = affiliateRepository.save(entity);
        return affiliateMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return affiliateRepository.findById(id)
                .map(affiliateMapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByDocument(String document) {
        return affiliateRepository.findByDocument(document)
                .map(affiliateMapper::toDomain);
    }

    @Override
    public List<Affiliate> findAll() {
        return affiliateRepository.findAll().stream()
                .map(affiliateMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        affiliateRepository.deleteById(id);
    }

    @Override
    public boolean existsByDocument(String document) {
        return affiliateRepository.existsByDocument(document);
    }
}