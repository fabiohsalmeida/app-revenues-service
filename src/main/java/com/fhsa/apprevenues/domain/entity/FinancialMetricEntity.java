package com.fhsa.apprevenues.domain.entity;

import com.fhsa.apprevenues.domain.enums.RiskRatingEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String date;

    @Column(nullable = false)
    private String appName;

    @Column(nullable = false)
    private Integer companyId;

    @Column(nullable = false)
    private BigDecimal totalRevenue;

    @Column
    private BigDecimal marketSpending;

    @Column
    private BigDecimal revenueAtTimeOfMarketingSpending;

    @Column
    private Integer paybackPeriod;

    @Column
    private Integer ltvCacRatio;

    @Column
    private Integer riskScore;

    @Column
    private String riskRating;

    @Column
    private Boolean isEvaluationFinished;
}
