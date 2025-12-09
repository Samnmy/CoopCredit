package com.coopcredit.creditapplicationservice.infrastructure.persistence.entity;

import com.coopcredit.creditapplicationservice.domain.model.AffiliateStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "affiliate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String document;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private LocalDate affiliationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AffiliateStatus status;

    @OneToMany(mappedBy = "affiliate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CreditApplicationEntity> creditApplications = new ArrayList<>();
}