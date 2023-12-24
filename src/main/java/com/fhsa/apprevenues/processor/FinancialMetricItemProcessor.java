package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.entity.FinancialMetricHistoryEntity;
import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import com.fhsa.apprevenues.repository.FinancialMetricHistoryRepository;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class FinancialMetricItemProcessor implements ItemProcessor<FinancialMetricItem, FinancialMetricEntity> {

    private final FinancialMetricRepository metricRepository;
    private final FinancialMetricHistoryRepository historyRepository;

    @Override
    @SneakyThrows
    public FinancialMetricEntity process(FinancialMetricItem financialMetricItem) {
        return alreadyProcessedInput(financialMetricItem) ?
                null :
                saveHistoryAndGetFixedMetricEntity(financialMetricItem);
    }

    private boolean alreadyProcessedInput(FinancialMetricItem item) {
        Optional<FinancialMetricHistoryEntity> storedFinancialMetric =
                historyRepository.findByDateAndAppNameAndCompanyId(
                        item.getDate(),
                        item.getAppName(),
                        item.getCompanyId()
                );

        return storedFinancialMetric.isPresent();
    }

    private FinancialMetricEntity saveHistoryAndGetFixedMetricEntity(FinancialMetricItem item) {
        saveFinancialMetricHistory(item);

        return getSetedFinancialMetricEntity(item);
    }

    private void saveFinancialMetricHistory(FinancialMetricItem item) {
        FinancialMetricHistoryEntity entity = FinancialMetricHistoryEntity.builder()
                .date(item.getDate())
                .yearMonth(getYearMonth(item.getDate()))
                .appName(item.getAppName())
                .companyId(item.getCompanyId())
                .revenue(item.getRevenue())
                .marketingSpend(getMarketingSpend(item))
                .build();

        historyRepository.save(entity);
    }

    private BigDecimal getMarketingSpend(FinancialMetricItem item) {
        return item.getMarketingSpend() != null ? item.getMarketingSpend() : BigDecimal.ZERO;
    }

    private FinancialMetricEntity getSetedFinancialMetricEntity(FinancialMetricItem item) {
        FinancialMetricEntity entity = getOrCreateFinancialMetricEntity(item);

        entity.setTotalRevenue(entity.getTotalRevenue().add(item.getRevenue()));

        if (hasNewMarketingSpend(item.getMarketingSpend())) {
            entity.setRevenueAtTimeOfMarketingSpend(entity.getTotalRevenue());
            entity.setMarketingSpend(item.getMarketingSpend());
            entity.setMarketingSpendDay(getDay(item.getDate()));
        } else if (alreadyHasMarketingSpendAndSearchingPaybackPeriod(entity.getMarketingSpend(), entity.getPaybackPeriod())) {
            BigDecimal actualRevenue = entity.getTotalRevenue().subtract(entity.getRevenueAtTimeOfMarketingSpend());

            if (actualRevenue.compareTo(entity.getMarketingSpend())>=0) {
                entity.setPaybackPeriod(getDay(item.getDate()) - entity.getMarketingSpendDay());
            }
        }

        return entity;
    }

    private Integer getDay(String date) {
        return Integer.valueOf(date.substring(date.length()-2));
    }

    private boolean alreadyHasMarketingSpendAndSearchingPaybackPeriod(BigDecimal marketingSpend, Integer paybackPeriod) {
        return marketingSpend != null && marketingSpend.compareTo(BigDecimal.ZERO) > 0 && paybackPeriod == null;
    }

    private static boolean hasNewMarketingSpend(BigDecimal marketingSpend) {
        return marketingSpend != null && marketingSpend.compareTo(BigDecimal.ZERO) > 0;
    }

    private FinancialMetricEntity getOrCreateFinancialMetricEntity(FinancialMetricItem item) {
        Optional<FinancialMetricEntity> entity =
                metricRepository.findByAppNameAndCompanyId(
                        item.getAppName(),
                        item.getCompanyId()
                );

        return entity.isPresent() ? entity.get() : createNewFinancialMetricEntity(item);
    }

    private FinancialMetricEntity createNewFinancialMetricEntity(FinancialMetricItem item) {
        return FinancialMetricEntity.builder()
                .yearMonth(getYearMonth(item.getDate()))
                .appName(item.getAppName())
                .companyId(item.getCompanyId())
                .totalRevenue(BigDecimal.ZERO)
                .marketingSpend(BigDecimal.ZERO)
                .isEvaluationFinished(Boolean.FALSE)
                .isAlreadyExported(Boolean.FALSE)
                .build();
    }

    private String getYearMonth(String date) {
        return date.substring(0, 7);
    }
}
