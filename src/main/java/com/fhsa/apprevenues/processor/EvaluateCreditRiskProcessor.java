package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.enums.RiskRatingEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

import static com.fhsa.apprevenues.domain.enums.RiskRatingEnum.*;
import static com.fhsa.apprevenues.util.EvaluateUtils.*;
import static java.math.RoundingMode.HALF_EVEN;

@RequiredArgsConstructor
public class EvaluateCreditRiskProcessor implements ItemProcessor<FinancialMetricEntity, FinancialMetricEntity> {

    @Override
    @SneakyThrows
    public FinancialMetricEntity process(FinancialMetricEntity entity) {
        BigDecimal ltvCacRatio = calculateLtvCacRatio(entity.getTotalRevenue(), entity.getMarketingSpend());
        BigDecimal riskScore = calculateCoefficientAndRiskScore(entity.getPaybackPeriod(), ltvCacRatio);
        RiskRatingEnum riskRating = evaluateRiskScore(riskScore);

        entity.setLtvCacRatio(ltvCacRatio);
        entity.setRiskScore(riskScore);
        entity.setRiskRating(riskRating.getValue());
        entity.setIsEvaluationFinished(Boolean.TRUE);

        return entity;
    }

    private RiskRatingEnum evaluateRiskScore(BigDecimal riskScore) {
        RiskRatingEnum riskRating;

        if (isBetween(riskScore, 85.0, 100.0)) {
            riskRating = UNDOUBTED;
        } else if (isBetween(riskScore, 65.0, 85.0)) {
            riskRating = LOW;
        } else if (isBetween(riskScore, 45.0, 65.0)) {
            riskRating = MODERATE;
        } else if (isBetween(riskScore, 25.0, 45.0)) {
            riskRating = CAUTIONARY;
        } else if (isBetween(riskScore, 15.0, 25.0)) {
            riskRating = UNSATISFACTORY;
        } else {
            riskRating = UNACCEPTABLE;
        }

        return riskRating;
    }

    private BigDecimal calculateCoefficientAndRiskScore(Integer paybackPeriod, BigDecimal ltvCacRatio) {
        Integer paybackPeriodRiskScore = evaluatePaybackPeriodRiskScore(paybackPeriod);
        Integer ltvCacRatioRiskScore = evaluateLtvCacRatioRiskScore(ltvCacRatio);

        return calculateRiskScore(
            BigDecimal.valueOf(paybackPeriodRiskScore),
            BigDecimal.valueOf(ltvCacRatioRiskScore)
        );
    }

    private BigDecimal calculateRiskScore(BigDecimal paybackPeriodRiskScore, BigDecimal ltvCacRatioRiskScore) {
        BigDecimal paybackCoefficient = paybackPeriodRiskScore.multiply(BigDecimal.valueOf(0.7));
        BigDecimal ltvCacCoefficient = ltvCacRatioRiskScore.multiply(BigDecimal.valueOf(0.3));

        return paybackCoefficient.add(ltvCacCoefficient);
    }

    private Integer evaluateLtvCacRatioRiskScore(BigDecimal ltvCacRatio) {
        Integer ltvCacRatioRiskScore;

        if (isLessThan(ltvCacRatio, 1.5)) {
            ltvCacRatioRiskScore = 10;
        } else if (isBetween(ltvCacRatio, 1.5, 2.0)) {
            ltvCacRatioRiskScore = 30;
        } else if (isBetween(ltvCacRatio, 2.0, 2.5)) {
            ltvCacRatioRiskScore = 60;
        } else if (isBetween(ltvCacRatio, 2.5, 3.0)) {
            ltvCacRatioRiskScore = 80;
        } else {
            ltvCacRatioRiskScore = 100;
        }

        return ltvCacRatioRiskScore;
    }

    private Integer evaluatePaybackPeriodRiskScore(Integer paybackPeriod) {
        Integer paybackRiskScore;

        if (isNeverReachablePaybackPeriod(paybackPeriod)) {
            paybackRiskScore = 10;
        } else if (isBetween(paybackPeriod, 21, 27)) {
            paybackRiskScore = 30;
        } else if (isBetween(paybackPeriod, 14, 21)) {
            paybackRiskScore = 60;
        } else if (isBetween(paybackPeriod, 7, 14)) {
            paybackRiskScore = 80;
        } else {
            paybackRiskScore = 100;
        }

        return paybackRiskScore;
    }

    private static boolean isNeverReachablePaybackPeriod(Integer paybackPeriod) {
        return paybackPeriod == null || isHigherThan(paybackPeriod, 27);
    }

    private BigDecimal calculateLtvCacRatio(BigDecimal totalRevenue, BigDecimal marketingSpend) {
        return BigDecimal.ZERO.compareTo(marketingSpend)==0 ?
                BigDecimal.valueOf(5.0) :
                totalRevenue.divide(marketingSpend, 2, HALF_EVEN);
    }
}
