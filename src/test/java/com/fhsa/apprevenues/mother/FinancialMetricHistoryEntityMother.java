package com.fhsa.apprevenues.mother;

import com.fhsa.apprevenues.domain.entity.FinancialMetricHistoryEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class FinancialMetricHistoryEntityMother {

    public static FinancialMetricHistoryEntity defaultFinancialMetricHistory() {
        return FinancialMetricHistoryEntity.builder()
            .id(1)
            .date("2023-12-24")
            .yearMonth("2023-12")
            .appName("Insane App")
            .companyId(100)
            .revenue(BigDecimal.TEN)
            .marketingSpend(BigDecimal.ONE)
            .build();
    }
}
