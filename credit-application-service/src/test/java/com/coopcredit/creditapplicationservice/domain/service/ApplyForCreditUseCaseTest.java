package com.coopcredit.creditapplicationservice.domain.service;

import com.coopcredit.creditapplicationservice.domain.model.*;
import com.coopcredit.creditapplicationservice.domain.port.AffiliatePort;
import com.coopcredit.creditapplicationservice.domain.port.CreditApplicationPort;
import com.coopcredit.creditapplicationservice.domain.port.RiskCentralPort;
import org.junit.jupiter.api.Test;
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
    private ApplyForCreditUseCase useCase;

    // ----------------------------------------------------------------------
    // ✔ 1. Test de flujo exitoso (Low Risk)
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_Success() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(8), 3000.0);

        CreditApplication application = createCreditApplication(affiliate, 5000.0, 10.0, 12);

        RiskEvaluation risk = new RiskEvaluation();
        risk.setScore(800);
        risk.setRiskLevel(RiskLevel.LOW);

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));
        when(riskCentralPort.evaluateRisk(any(), any(), any())).thenReturn(risk);
        when(creditApplicationPort.save(any())).thenReturn(application);

        CreditApplication result = useCase.execute(application);

        assertEquals(ApplicationStatus.APPROVED, result.getStatus());
        verify(creditApplicationPort, times(1)).save(any());
    }

    // ----------------------------------------------------------------------
    // ✔ 2. Affiliate NO existe
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_AffiliateNotFound() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(10), 3000.0);
        CreditApplication app = createCreditApplication(affiliate, 5000.0, 10.0, 12);

        when(affiliatePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(app)
        );
    }

    // ----------------------------------------------------------------------
    // ✔ 3. Affiliate NO está activo
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_AffiliateNotActive() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(10), 3000.0);
        affiliate.setStatus(AffiliateStatus.INACTIVE);

        CreditApplication app = createCreditApplication(affiliate, 4000.0, 10.0, 12);

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> useCase.execute(app));

        assertEquals("Affiliate is not active", exception.getMessage());
    }

    // ----------------------------------------------------------------------
    // ✔ 4. Afiliación menor a 6 meses
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_MinAffiliationTime_NotMet() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(5), 3000.0);

        CreditApplication app = createCreditApplication(affiliate, 4000.0, 10.0, 12);

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(app)
        );

        assertEquals("Affiliate does not meet minimum affiliation time (6 months)", ex.getMessage());
    }

    // ----------------------------------------------------------------------
    // ✔ 5. Cuota mensual supera 30% del salario
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_MonthlyPaymentExceedsLimit() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(8), 2000.0);

        CreditApplication app = createCreditApplication(affiliate, 20000.0, 15.0, 12);

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(app));
    }

    // ----------------------------------------------------------------------
    // ✔ 6. Riesgo ALTO → Rechazado
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_HighRisk_Rejected() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(8), 3000.0);

        CreditApplication app = createCreditApplication(affiliate, 4000.0, 10.0, 12);

        RiskEvaluation risk = new RiskEvaluation();
        risk.setRiskLevel(RiskLevel.HIGH);

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));
        when(riskCentralPort.evaluateRisk(any(), any(), any())).thenReturn(risk);
        when(creditApplicationPort.save(any())).thenReturn(app);

        CreditApplication result = useCase.execute(app);

        assertEquals(ApplicationStatus.REJECTED, result.getStatus());
        assertEquals("High risk level from risk central", result.getRiskEvaluation().getDetails());
    }

    // ----------------------------------------------------------------------
    // ✔ 7. Riesgo MEDIO → Rechazado si cuota supera 25%
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_MediumRisk_Rejected_WhenPaymentTooHigh() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(8), 2000.0);

        CreditApplication app = createCreditApplication(affiliate, 10000.0, 15.0, 12);

        RiskEvaluation risk = new RiskEvaluation();
        risk.setRiskLevel(RiskLevel.MEDIUM);

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));
        when(riskCentralPort.evaluateRisk(any(), any(), any())).thenReturn(risk);
        when(creditApplicationPort.save(any())).thenReturn(app);

        CreditApplication result = useCase.execute(app);

        assertEquals(ApplicationStatus.REJECTED, result.getStatus());
    }

    // ----------------------------------------------------------------------
    // ✔ 8. Riesgo MEDIO → Aprobado si cuota <= 25%
    // ----------------------------------------------------------------------
    @Test
    void testApplyForCredit_MediumRisk_Approved() {
        Affiliate affiliate = createAffiliate(LocalDate.now().minusMonths(8), 5000.0);

        CreditApplication app = createCreditApplication(affiliate, 2000.0, 10.0, 12);

        RiskEvaluation risk = new RiskEvaluation();
        risk.setRiskLevel(RiskLevel.MEDIUM);

        when(affiliatePort.findById(1L)).thenReturn(Optional.of(affiliate));
        when(riskCentralPort.evaluateRisk(any(), any(), any())).thenReturn(risk);
        when(creditApplicationPort.save(any())).thenReturn(app);

        CreditApplication result = useCase.execute(app);

        assertEquals(ApplicationStatus.APPROVED, result.getStatus());
    }

    // ----------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------
    private Affiliate createAffiliate(LocalDate affiliationDate, double salary) {
        Affiliate a = new Affiliate();
        a.setId(1L);
        a.setDocument("12345");
        a.setAffiliationDate(affiliationDate);
        a.setSalary(salary);
        a.setStatus(AffiliateStatus.ACTIVE);
        return a;
    }

    private CreditApplication createCreditApplication(Affiliate a, double amount, double rate, int months) {
        CreditApplication c = new CreditApplication();
        c.setAffiliate(a);
        c.setRequestedAmount(BigDecimal.valueOf(amount));
        c.setProposedRate(rate);
        c.setTermMonths(months);
        return c;
    }
}
