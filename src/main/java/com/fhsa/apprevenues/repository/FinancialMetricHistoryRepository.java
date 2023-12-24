package com.fhsa.apprevenues.repository;

import com.fhsa.apprevenues.domain.entity.FinancialMetricHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialMetricHistoryRepository extends JpaRepository<FinancialMetricHistoryEntity, Integer> {

    Optional<FinancialMetricHistoryEntity> findByDateAndAppNameAndCompanyId(String date, String appName, Integer companyId);
}
