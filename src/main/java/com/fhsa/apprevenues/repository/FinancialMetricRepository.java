package com.fhsa.apprevenues.repository;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialMetricRepository extends JpaRepository<FinancialMetricEntity, Integer> {

    FinancialMetricEntity findByDateAndAppNameAndCompanyId(String date, String appName, Integer companyId);
}
