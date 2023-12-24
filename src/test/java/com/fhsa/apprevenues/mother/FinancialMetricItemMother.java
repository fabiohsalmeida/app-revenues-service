package com.fhsa.apprevenues.mother;

import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import com.fhsa.apprevenues.domain.item.FinancialMetricItem.FinancialMetricItemBuilder;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class FinancialMetricItemMother {

    public static final String DEFAULT_DATE = "2023-12-24";
    public static final Integer DEFAULT_FINANCIAL_METRIC_ITEM_DAY = 24;
    public static final String DEFAULT_APP_NAME = "Insane App";
    public static final Integer DEFAULT_COMPANY_ID = 100;
    public static final BigDecimal DEFAULT_REVENUE = BigDecimal.valueOf(10000);
    public static final BigDecimal DEFAULT_MARKETING_SPEND = BigDecimal.ZERO;

    public static final FinancialMetricItem defaultFinancialMetricItem() {
        return preset()
            .date(DEFAULT_DATE)
            .revenue(DEFAULT_REVENUE)
            .marketingSpend(DEFAULT_MARKETING_SPEND)
            .build();
    }

    public static FinancialMetricItem customFinancialMetricItem(
            BigDecimal revenue,
            BigDecimal marketingSpend
    ) {
        return preset()
                .date(DEFAULT_DATE)
                .revenue(revenue)
                .marketingSpend(marketingSpend)
                .build();
    }

    public static FinancialMetricItem customFinancialMetricItem(
            BigDecimal revenue
    ) {
        return preset()
                .date(DEFAULT_DATE)
                .revenue(revenue)
                .marketingSpend(DEFAULT_MARKETING_SPEND)
                .build();
    }

    private static FinancialMetricItemBuilder preset() {
        return FinancialMetricItem.builder()
            .appName(DEFAULT_APP_NAME)
            .companyId(DEFAULT_COMPANY_ID);
    }

}
