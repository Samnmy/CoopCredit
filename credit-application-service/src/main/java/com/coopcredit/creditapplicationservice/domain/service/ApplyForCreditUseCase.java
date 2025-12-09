package com.coopcredit.creditapplicationservice.domain.service;

import com.coopcredit.creditapplicationservice.domain.model.*;
import com.coopcredit.creditapplicationservice.domain.port.AffiliatePort;
import com.coopcredit.creditapplicationservice.domain.port.CreditApplicationPort;
import com.coopcredit.creditapplicationservice.domain.port.RiskCentralPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ApplyForCreditUseCase {

    private final CreditApplicationPort creditApplicationPort;
    private final AffiliatePort affiliatePort;
    private final RiskCentralPort riskCentralPort;

    public ApplyForCreditUseCase(CreditApplicationPort creditApplicationPort,
                                 AffiliatePort affiliatePort,
                                 RiskCentralPort riskCentralPort) {
        this.creditApplicationPort = creditApplicationPort;
        this.affiliatePort = affiliatePort;
        this.riskCentralPort = riskCentralPort;
    }

    @Transactional
    public CreditApplication execute(CreditApplication creditApplication) {
        // Validar que el afiliado exista y esté activo
        Affiliate affiliate = affiliatePort.findById(creditApplication.getAffiliate().getId())
                .orElseThrow(() -> new IllegalArgumentException("Affiliate not found"));

        if (affiliate.getStatus() != AffiliateStatus.ACTIVE) {
            throw new IllegalArgumentException("Affiliate is not active");
        }

        // Validaciones de negocio
        if (!validateMinAffiliationTime(affiliate)) {
            throw new IllegalArgumentException("Affiliate does not meet minimum affiliation time (6 months)");
        }

        // Calcular relación cuota/ingreso
        double monthlyPayment = calculateMonthlyPayment(
                creditApplication.getRequestedAmount().doubleValue(),
                creditApplication.getProposedRate(),
                creditApplication.getTermMonths()
        );

        if (monthlyPayment > (affiliate.getSalary() * 0.3)) {
            throw new IllegalArgumentException("Monthly payment exceeds 30% of salary");
        }

        // Establecer fecha de solicitud y estado inicial
        creditApplication.setApplicationDate(LocalDate.now());
        creditApplication.setStatus(ApplicationStatus.PENDING);

        // Invocar al servicio externo de riesgo
        RiskEvaluation riskEvaluation = riskCentralPort.evaluateRisk(
                affiliate.getDocument(),
                creditApplication.getRequestedAmount().doubleValue(),
                creditApplication.getTermMonths()
        );

        // Evaluar la solicitud basada en el riesgo
        if (riskEvaluation.getRiskLevel() == RiskLevel.HIGH) {
            creditApplication.setStatus(ApplicationStatus.REJECTED);
            riskEvaluation.setDetails("High risk level from risk central");
        } else if (riskEvaluation.getRiskLevel() == RiskLevel.MEDIUM) {
            // Aplicar políticas adicionales para riesgo medio
            if (monthlyPayment > (affiliate.getSalary() * 0.25)) {
                creditApplication.setStatus(ApplicationStatus.REJECTED);
                riskEvaluation.setDetails("Monthly payment too high for medium risk");
            } else {
                creditApplication.setStatus(ApplicationStatus.APPROVED);
                riskEvaluation.setDetails("Credit approved with medium risk");
            }
        } else {
            creditApplication.setStatus(ApplicationStatus.APPROVED);
            riskEvaluation.setDetails("Credit approved with low risk");
        }

        // SOLUCIÓN: Solo establecer la relación en una dirección
        // Comenta o elimina esta línea (83):
        // riskEvaluation.setCreditApplication(creditApplication);

        // Mantén esta línea (85) que establece la relación correctamente:
        creditApplication.setRiskEvaluation(riskEvaluation);

        return creditApplicationPort.save(creditApplication);
    }

    private boolean validateMinAffiliationTime(Affiliate affiliate) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        return affiliate.getAffiliationDate().isBefore(sixMonthsAgo)
                || affiliate.getAffiliationDate().isEqual(sixMonthsAgo);
    }

    private double calculateMonthlyPayment(double amount, double annualRate, int months) {
        double monthlyRate = annualRate / 100 / 12;
        return (amount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);
    }
}