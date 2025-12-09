package com.coopcredit.creditapplicationservice.infrastructure.persistence.entity;

import com.coopcredit.creditapplicationservice.domain.model.RiskLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "risk_evaluation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    @Column(columnDefinition = "TEXT")
    private String details;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_application_id", nullable = false, unique = true)
    private CreditApplicationEntity creditApplication;
}