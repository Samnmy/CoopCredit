package com.coopcredit.creditapplicationservice.infrastructure.persistence.repository;

import com.coopcredit.creditapplicationservice.infrastructure.persistence.entity.CreditApplicationEntity;
import com.coopcredit.creditapplicationservice.domain.model.ApplicationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditApplicationRepository extends JpaRepository<CreditApplicationEntity, Long> {

    @EntityGraph(attributePaths = {"affiliate", "riskEvaluation"})
    Optional<CreditApplicationEntity> findById(Long id);

    @Query("SELECT ca FROM CreditApplicationEntity ca WHERE ca.affiliate.id = :affiliateId")
    List<CreditApplicationEntity> findByAffiliateId(@Param("affiliateId") Long affiliateId);

    @Query("SELECT ca FROM CreditApplicationEntity ca WHERE ca.status = :status")
    List<CreditApplicationEntity> findByStatus(@Param("status") ApplicationStatus status);

    @Query("SELECT ca FROM CreditApplicationEntity ca " +
            "LEFT JOIN FETCH ca.affiliate " +
            "LEFT JOIN FETCH ca.riskEvaluation " +
            "WHERE ca.id = :id")
    Optional<CreditApplicationEntity> findByIdWithDetails(@Param("id") Long id);
}