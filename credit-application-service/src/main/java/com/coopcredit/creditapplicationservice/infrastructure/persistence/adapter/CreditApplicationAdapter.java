// CreditApplicationAdapter.java
package com.coopcredit.creditapplicationservice.infrastructure.persistence.adapter;

import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import com.coopcredit.creditapplicationservice.domain.model.ApplicationStatus;
import com.coopcredit.creditapplicationservice.domain.port.CreditApplicationPort;
import com.coopcredit.creditapplicationservice.application.mapper.CreditApplicationMapper;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.repository.CreditApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreditApplicationAdapter implements CreditApplicationPort {

    private final CreditApplicationRepository creditApplicationRepository;
    private final CreditApplicationMapper creditApplicationMapper;

    @Override
    public CreditApplication save(CreditApplication creditApplication) {
        var entity = creditApplicationMapper.toEntity(creditApplication);
        var savedEntity = creditApplicationRepository.save(entity);
        return creditApplicationMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CreditApplication> findById(Long id) {
        return creditApplicationRepository.findById(id)
                .map(creditApplicationMapper::toDomain);
    }

    @Override
    public List<CreditApplication> findAll() {
        return creditApplicationRepository.findAll().stream()
                .map(creditApplicationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findByAffiliateId(Long affiliateId) {
        return creditApplicationRepository.findByAffiliateId(affiliateId).stream()
                .map(creditApplicationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findByStatus(ApplicationStatus status) {
        return creditApplicationRepository.findByStatus(status).stream()
                .map(creditApplicationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        creditApplicationRepository.deleteById(id);
    }
}