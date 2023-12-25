package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.enums.RiskRatingEnum;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.fhsa.apprevenues.domain.enums.RiskRatingEnum.*;
import static com.fhsa.apprevenues.mother.FinancialMetricEntityMother.readyToBeEvaluatedMetricEntity;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class EvaluateCreditRiskProcessorTest {

    private static final BigDecimal DEFAULT_TOTAL_REVENUE = BigDecimal.valueOf(200000);
    private static final BigDecimal DEFAULT_MARKETING_SPEND = BigDecimal.valueOf(100000);
    private static final Integer DEFAULT_PAYBACK_PERIOD = 10;

    @InjectMocks
    private EvaluateCreditRiskProcessor processor;

    @ParameterizedTest
    @ValueSource(strings = {"350000", "270000", "245000", "195000", "120000"})
    void validStateLtvCacRatioRating(String arg) {
        BigDecimal totalRevenue = BigDecimal.valueOf(Long.parseLong(arg));
        FinancialMetricEntity input = readyToBeEvaluatedMetricEntity(
            totalRevenue,
            DEFAULT_MARKETING_SPEND,
            DEFAULT_PAYBACK_PERIOD
        );

        FinancialMetricEntity response = processor.process(input);

        BigDecimal expectedLtvCacRatio = totalRevenue.divide(DEFAULT_MARKETING_SPEND, 2, HALF_EVEN);
        assertTrue(expectedLtvCacRatio.compareTo(response.getLtvCacRatio())==0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"5", "11", "17", "25", "30"})
    void validStatePaybackPeriodRating(String arg) {
        Integer paybackPeriod = Integer.valueOf(arg);
        FinancialMetricEntity input = readyToBeEvaluatedMetricEntity(
                DEFAULT_TOTAL_REVENUE,
                DEFAULT_MARKETING_SPEND,
                paybackPeriod
        );

        FinancialMetricEntity response = processor.process(input);
        assertEquals(paybackPeriod, response.getPaybackPeriod());
    }

    @ParameterizedTest
    @MethodSource("provideParametersForRiskRating")
    void validStateModerateRiskRating(
            BigDecimal totalRevenue,
            BigDecimal marketingSpend,
            Integer paybackPeriod,
            RiskRatingEnum expectedRating
    ) {
        FinancialMetricEntity input = readyToBeEvaluatedMetricEntity(
                totalRevenue,
                marketingSpend,
                paybackPeriod
        );

        FinancialMetricEntity response = processor.process(input);

        assertEquals(expectedRating.getValue(), response.getRiskRating());
    }

    private static Stream<Arguments> provideParametersForRiskRating() {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(500000), BigDecimal.valueOf(36159), 10, UNDOUBTED),
            Arguments.of(BigDecimal.valueOf(823902), BigDecimal.valueOf(350000), 13, LOW),
            Arguments.of(BigDecimal.valueOf(79675), BigDecimal.valueOf(40000), 15, MODERATE),
            Arguments.of(BigDecimal.valueOf(79675), BigDecimal.valueOf(40000), 22, CAUTIONARY),
            Arguments.of(BigDecimal.valueOf(40000), BigDecimal.valueOf(40000), 22, UNSATISFACTORY),
            Arguments.of(BigDecimal.valueOf(67418), BigDecimal.valueOf(100000), null, UNACCEPTABLE)
        );
    }
}
