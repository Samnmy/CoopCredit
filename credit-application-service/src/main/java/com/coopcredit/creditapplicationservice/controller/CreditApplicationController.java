package com.coopcredit.creditapplicationservice.controller;

import com.coopcredit.creditapplicationservice.application.dto.CreditApplicationRequest;
import com.coopcredit.creditapplicationservice.application.dto.CreditApplicationResponse;
import com.coopcredit.creditapplicationservice.application.mapper.CreditApplicationMapper;
import com.coopcredit.creditapplicationservice.domain.service.ApplyForCreditUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/credit-applications")
@RequiredArgsConstructor
public class CreditApplicationController {

    private final ApplyForCreditUseCase applyForCreditUseCase;
    private final CreditApplicationMapper creditApplicationMapper;

    @PostMapping
    @PreAuthorize("hasRole('AFFILIATE')")
    public ResponseEntity<CreditApplicationResponse> create(
            @Valid @RequestBody CreditApplicationRequest request) {

        var creditApplication = creditApplicationMapper.toDomain(request);
        var result = applyForCreditUseCase.execute(creditApplication);
        var response = creditApplicationMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AFFILIATE', 'ANALYST', 'ADMIN')")
    public ResponseEntity<CreditApplicationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/affiliate/{affiliateId}")
    @PreAuthorize("hasRole('AFFILIATE') and #affiliateId == authentication.principal.affiliateId or hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<CreditApplicationResponse>> getByAffiliate(
            @PathVariable Long affiliateId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<CreditApplicationResponse>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok().build();
    }
}