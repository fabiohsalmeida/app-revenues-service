package com.fhsa.apprevenues.repository;

import com.fhsa.apprevenues.domain.entity.FinancialMonthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialMonthRepository extends JpaRepository<FinancialMonthEntity, Integer> {

    Optional<FinancialMonthEntity> findByYearMonth(String yearMonth);
    List<FinancialMonthEntity> findByIsProcessed(Boolean isProcessed);
}
