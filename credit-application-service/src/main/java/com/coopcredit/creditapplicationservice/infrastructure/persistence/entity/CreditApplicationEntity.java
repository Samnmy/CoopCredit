package com.coopcredit.creditapplicationservice.infrastructure.persistence.entity;

import com.coopcredit.creditapplicationservice.domain.model.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credit_application")
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "CreditApplication.withAffiliateAndEvaluation",
        attributeNodes = {
                @NamedAttributeNode("affiliate"),
                @NamedAttributeNode("riskEvaluation")
        }
)
public class CreditApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id", nullable = false)
    private AffiliateEntity affiliate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    @Column(nullable = false)
    private Integer termMonths;

    @Column(nullable = false)
    private Double proposedRate;

    @Column(nullable = false)
    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @OneToOne(mappedBy = "creditApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RiskEvaluationEntity riskEvaluation;
}