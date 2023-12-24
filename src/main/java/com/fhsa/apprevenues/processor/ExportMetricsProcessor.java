package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.item.EvaluatedRiskScoreAppCompanyItem;
import com.fhsa.apprevenues.repository.CompanyRepository;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class ExportMetricsProcessor implements
        ItemProcessor<FinancialMetricEntity, EvaluatedRiskScoreAppCompanyItem> {

    private final CompanyRepository companyRepository;
    private final FinancialMetricRepository metricRepository;

    @Override
    @SneakyThrows
    public EvaluatedRiskScoreAppCompanyItem process(FinancialMetricEntity entity) {
        return EvaluatedRiskScoreAppCompanyItem.builder()
            .companyId(entity.getCompanyId())
            .companyName(getCompanyName(entity))
            .appName(entity.getAppName())
            .riskScore(entity.getRiskScore().intValue())
            .riskRating(entity.getRiskRating())
            .build();
    }

    private String getCompanyName(FinancialMetricEntity entity) {
        return companyRepository.findById(entity.getCompanyId()).get().getName();
    }
}
