package com.fhsa.apprevenues.mother;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity.FinancialMetricEntityBuilder;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class FinancialMetricEntityMother {

    private final static Integer DEFAULT_ID = 1;
    private final static String DEFAULT_YEAR_MONTH = "2023-12";
    private final static String DEFAULT_APP_NAME = "Insane App";
    private final static Integer DEFAULT_COMPANY_ID = 100;
    private final static Boolean DEFAULT_IS_EVALUATION_FINISHED = Boolean.FALSE;
    private final static Boolean DEFAULT_IS_ALREADY_EXPORTED = Boolean.FALSE;

    private static final BigDecimal DEFAULT_TOTAL_REVENUE = BigDecimal.valueOf(300000);
    private static final BigDecimal DEFAULT_REVENUE_AT_TIME_OF_MARKETING_SPEND = BigDecimal.valueOf(1000);
    private static final BigDecimal DEFAULT_MARKETING_SPEND = BigDecimal.valueOf(10000);
    private static final Integer DEFAULT_MARKETING_SPEND_DAY = 1;

    public static FinancialMetricEntity entityWithoutMarketingSpend(
        BigDecimal totalRevenue
    ) {
        return presetBuilder()
            .totalRevenue(totalRevenue)
            .build();
    }

    public static FinancialMetricEntity entityWithMarketingSpend (
        BigDecimal totalRevenue,
        BigDecimal marketingSpend,
        Integer marketingSpendDay,
        BigDecimal revenueAtTimeOfMarketingSpend
    ) {
        return presetBuilder()
            .totalRevenue(totalRevenue)
            .marketingSpend(marketingSpend)
            .marketingSpendDay(marketingSpendDay)
            .revenueAtTimeOfMarketingSpend(revenueAtTimeOfMarketingSpend)
            .build();
    }

    public static FinancialMetricEntity entityWithBreakEven(
        BigDecimal totalRevenue,
        BigDecimal marketingSpend,
        Integer marketingSpendDay,
        BigDecimal revenueAtTimeOfMarketingSpend,
        Integer paybackPeriod
    ) {
        return presetBuilder()
            .totalRevenue(totalRevenue)
            .marketingSpend(marketingSpend)
            .marketingSpendDay(marketingSpendDay)
            .revenueAtTimeOfMarketingSpend(revenueAtTimeOfMarketingSpend)
            .paybackPeriod(paybackPeriod)
            .build();
    }

    private static FinancialMetricEntityBuilder presetBuilder() {
        return FinancialMetricEntity.builder()
            .id(DEFAULT_ID)
            .yearMonth(DEFAULT_YEAR_MONTH)
            .appName(DEFAULT_APP_NAME)
            .companyId(DEFAULT_COMPANY_ID)
            .isEvaluationFinished(DEFAULT_IS_EVALUATION_FINISHED)
            .isAlreadyExported(DEFAULT_IS_ALREADY_EXPORTED);
    }
}
