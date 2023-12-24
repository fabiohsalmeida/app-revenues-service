package com.fhsa.apprevenues.domain.entity;

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
@Table(name = "financial_metrics_history")
public class FinancialMetricHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String yearMonth;

    @Column(nullable = false)
    private String appName;

    @Column(nullable = false)
    private Integer companyId;

    @Column(nullable = false)
    private BigDecimal revenue;

    @Column(nullable = false)
    private BigDecimal marketingSpend;
}
