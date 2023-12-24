package com.fhsa.apprevenues.repository;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialMetricRepository extends JpaRepository<FinancialMetricEntity, Integer> {

    Optional<FinancialMetricEntity> findByAppNameAndCompanyId(String appName, Integer companyId);
    Page<FinancialMetricEntity> findByIsAlreadyExported(Boolean isAlreadyExported, Pageable pageable);
    Page<FinancialMetricEntity> findByIsEvaluationFinished(Boolean isEvaluationFinished, Pageable pageable);
}
