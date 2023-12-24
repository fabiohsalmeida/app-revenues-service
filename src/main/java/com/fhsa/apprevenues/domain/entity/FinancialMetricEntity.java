package com.fhsa.apprevenues.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "financial_metrics")
public class FinancialMetricEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String yearMonth;

    @Column(nullable = false)
    private String appName;

    @Column(nullable = false)
    private Integer companyId;

    @Setter
    @Column(nullable = false)
    private BigDecimal totalRevenue;

    @Setter
    @Column
    private BigDecimal marketingSpend;

    @Setter
    @Column
    private BigDecimal revenueAtTimeOfMarketingSpend;

    @Setter
    @Column
    private Integer marketingSpendDay;

    @Setter
    @Column
    private Integer paybackPeriod;

    @Setter
    @Column
    private Integer ltvCacRatio;

    @Setter
    @Column
    private Integer riskScore;

    @Setter
    @Column
    private String riskRating;

    @Setter
    @Column
    private Boolean isEvaluationFinished;
}
