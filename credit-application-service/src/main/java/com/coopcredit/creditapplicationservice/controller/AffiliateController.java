package com.coopcredit.creditapplicationservice.controller;

import com.coopcredit.creditapplicationservice.application.dto.AffiliateRequest;
import com.coopcredit.creditapplicationservice.application.dto.AffiliateResponse;
import com.coopcredit.creditapplicationservice.application.mapper.AffiliateMapper;
import com.coopcredit.creditapplicationservice.domain.service.RegisterAffiliateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/affiliates")
@RequiredArgsConstructor
public class AffiliateController {

    private final RegisterAffiliateUseCase registerAffiliateUseCase;
    private final AffiliateMapper affiliateMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AffiliateResponse> create(@Valid @RequestBody AffiliateRequest request) {
        var affiliate = affiliateMapper.toDomain(request);
        var result = registerAffiliateUseCase.execute(affiliate);
        var response = affiliateMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<AffiliateResponse>> getAll() {
        // Implementar si es necesario
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AFFILIATE', 'ANALYST', 'ADMIN')")
    public ResponseEntity<AffiliateResponse> getById(@PathVariable Long id) {
        // Implementar si es necesario
        return ResponseEntity.ok().build();
    }
}