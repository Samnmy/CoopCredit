package com.coopcredit.creditapplicationservice.domain.service;

import com.coopcredit.creditapplicationservice.domain.model.*;
import com.coopcredit.creditapplicationservice.domain.port.AffiliatePort;
import com.coopcredit.creditapplicationservice.domain.port.CreditApplicationPort;
import com.coopcredit.creditapplicationservice.domain.port.RiskCentralPort;
import org.testng.annotations.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApplyForCreditUseCaseTest {

    @Mock
    private CreditApplicationPort creditApplicationPort;

    @Mock
    private AffiliatePort affiliatePort;

    @Mock
    private RiskCentralPort riskCentralPort;

    @InjectMocks
    private ApplyForCreditUseCase applyForCreditUseCase;

    @Test
    void testApplyForCredit_Success() {
        // Arrange
        Affiliate affiliate = new Affiliate();
        affiliate.setId(1L);
        affiliate.setDocument("123456789");
        affiliate.setSalary(3000.0);
        affiliate.setAffiliationDate(LocalDate.now().minusMonths(7));
        affiliate.setStatus(AffiliateStatus.ACTIVE);

        CreditApplication creditApplication = new CreditApplication();
        creditApplication.setAffiliate(affiliate);
        creditApplication.setRequestedAmount(new BigDecimal("5000"));
        creditApplication.setTermMonths(12);
        creditApplication.setProposedRate(10.0);

        RiskEvaluation riskEvaluation = new RiskEvaluation();
        riskEvaluation.setScore(800);
        riskEvaluation.setRiskLevel(RiskLevel.LOW);
        riskEvaluation.setDetails("Good credit history");

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));
        when(riskCentralPort.evaluateRisk(any(), any(), any())).thenReturn(riskEvaluation);
        when(creditApplicationPort.save(any())).thenReturn(creditApplication);

        // Act
        CreditApplication result = applyForCreditUseCase.execute(creditApplication);

        // Assert
        assertNotNull(result);
        assertEquals(ApplicationStatus.APPROVED, result.getStatus());
        verify(creditApplicationPort, times(1)).save(any());
    }
}