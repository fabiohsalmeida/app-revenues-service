package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.item.EvaluatedRiskScoreAppCompanyItem;
import com.fhsa.apprevenues.exception.CompanyNotFoundException;
import com.fhsa.apprevenues.repository.CompanyRepository;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

import java.util.Optional;

@RequiredArgsConstructor
public class ExportMetricsProcessor implements
        ItemProcessor<FinancialMetricEntity, EvaluatedRiskScoreAppCompanyItem> {

    private final CompanyRepository companyRepository;
    private final FinancialMetricRepository metricRepository;

    @Override
    @SneakyThrows
    public EvaluatedRiskScoreAppCompanyItem process(FinancialMetricEntity entity) {
        updateIsExportedField(entity);

        return EvaluatedRiskScoreAppCompanyItem.builder()
            .companyId(entity.getCompanyId())
            .companyName(getCompanyName(entity.getCompanyId()))
            .appName(entity.getAppName())
            .riskScore(entity.getRiskScore().intValue())
            .riskRating(entity.getRiskRating())
            .build();
    }

    private void updateIsExportedField(FinancialMetricEntity entity) {
        entity.setIsAlreadyExported(Boolean.TRUE);

        metricRepository.save(entity);
    }

    private String getCompanyName(Integer companyId) {
        Optional<CompanyEntity> entityOptional = companyRepository.findById(companyId);

        if (entityOptional.isEmpty()) {
            throw new CompanyNotFoundException(companyId);
        }

        return entityOptional.get().getName();
    }
}
