package com.fhsa.apprevenues.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialMetricItem {

    private String date;
    private String appName;
    private Integer companyId;
    private Float revenue;
    private Float marketingSpend;
}
