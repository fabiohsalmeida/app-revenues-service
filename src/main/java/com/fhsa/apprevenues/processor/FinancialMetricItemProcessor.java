package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

public class FinancialMetricItemProcessor implements ItemProcessor<FinancialMetricItem, FinancialMetricEntity> {

    @Override
    @SneakyThrows
    public FinancialMetricEntity process(FinancialMetricItem financialMetricItem) {
        return new FinancialMetricEntity();
    }
}
