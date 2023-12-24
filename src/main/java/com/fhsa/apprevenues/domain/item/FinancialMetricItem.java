package com.fhsa.apprevenues.domain.item;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialMetricItem {

    private String date;
    private String appName;
    private Integer companyId;
    private BigDecimal revenue;
    private BigDecimal marketingSpend;
}
