package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.repository.FinancialMetricHistoryRepository;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class EvaluateCreditRiskProcessor implements ItemProcessor<FinancialMetricEntity, FinancialMetricEntity> {

    private final FinancialMetricRepository metricRepository;
    private final FinancialMetricHistoryRepository historyRepository;

    @Override
    @SneakyThrows
    public FinancialMetricEntity process(FinancialMetricEntity entity) {
        System.out.println("AppName+CompanyId:"+entity.getAppName()+entity.getCompanyId()+", Payback:"+entity.getPaybackPeriod()+", isEvaluationFinished:"+entity.getIsEvaluationFinished());

        return entity;
    }
}
