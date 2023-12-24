package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

public class FinancialMetricItemProcessor implements ItemProcessor<FinancialMetricItem, FinancialMetricItem> {

    @Override
    @SneakyThrows
    public FinancialMetricItem process(FinancialMetricItem financialMetricItem) {
        return financialMetricItem;
    }
}
