package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.entity.FinancialMetricHistoryEntity;
import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import com.fhsa.apprevenues.repository.FinancialMetricHistoryRepository;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.fhsa.apprevenues.mother.FinancialMetricEntityMother.*;
import static com.fhsa.apprevenues.mother.FinancialMetricHistoryEntityMother.defaultFinancialMetricHistory;
import static com.fhsa.apprevenues.mother.FinancialMetricItemMother.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinancialMetricItemProcessorTest {

    private static final BigDecimal DEFAULT_TOTAL_REVENUE = BigDecimal.valueOf(1000);
    private static final BigDecimal DEFAULT_REVENUE_AT_TIME_OF_MARKETING_SPEND = BigDecimal.valueOf(100);
    private static final BigDecimal DEFAULT_MARKETING_SPEND = BigDecimal.valueOf(10000);
    private static final Integer DEFAULT_MARKETING_SPEND_DAY = 1;
    private static final Integer PAYBACK_PERIOD = 8;

    @InjectMocks
    private FinancialMetricItemProcessor processor;

    @Mock
    private FinancialMetricRepository metricRepository;

    @Mock
    private FinancialMetricHistoryRepository historyRepository;

    @Test
    void invalidStateAlreadyHasInput() {
        var input = defaultFinancialMetricItem();
        Optional<FinancialMetricHistoryEntity> mockedHistory = Optional.of(defaultFinancialMetricHistory());

        when(historyRepository.findByDateAndAppNameAndCompanyId(
                eq(input.getDate()), eq(input.getAppName()), eq(input.getCompanyId()))
        ).thenReturn(mockedHistory);

        FinancialMetricEntity response = processor.process(input);

        assertNull(response);
    }

    @Test
    void validStateNewMetricFinancial() {
        var input = defaultFinancialMetricItem();
        Optional<FinancialMetricEntity> mockedMetric = Optional.empty();

        mockFinancialMetricHistory(input);
        mockFinancialMetric(input, mockedMetric);

        var response = processor.process(input);

        verifyNewEntryToMetricHistory();

        assertMetricTotalRevenueValue(input, BigDecimal.ZERO, response);
        assertNull(response.getRevenueAtTimeOfMarketingSpend());
        assertEquals(BigDecimal.ZERO, response.getMarketingSpend());
        assertNull(response.getMarketingSpendDay());
        assertNotNull(response.getYearMonth());
        assertEquals(input.getAppName(), response.getAppName());
        assertEquals(input.getCompanyId(), response.getCompanyId());
        assertNull(response.getPaybackPeriod());
        assertNull(response.getLtvCacRatio());
        assertNull(response.getRiskScore());
        assertNull(response.getRiskRating());
        assertFalse(response.getIsEvaluationFinished());
        assertFalse(response.getIsAlreadyExported());
    }

    @Test
    void validStateNewInputDoesntHaveMarketingSpend() {
        var input = defaultFinancialMetricItem();
        var mockedMetric =
                Optional.of(entityWithoutMarketingSpend(DEFAULT_TOTAL_REVENUE));

        mockFinancialMetricHistory(input);
        mockFinancialMetric(input, mockedMetric);

        var response = processor.process(input);

        verifyNewEntryToMetricHistory();

        assertMetricTotalRevenueValue(input, DEFAULT_TOTAL_REVENUE, response);
        assertNull(response.getRevenueAtTimeOfMarketingSpend());
        assertNull(response.getMarketingSpend());
        assertNull(response.getMarketingSpendDay());
        assertNull(response.getPaybackPeriod());
        assertUnchangableFields(mockedMetric.get(), response);
    }

    @Test
    void validStateNewInputAndNewMarketingSpend() {
        var input = customFinancialMetricItem(BigDecimal.valueOf(12000), DEFAULT_MARKETING_SPEND);
        var mockedMetric = Optional.of(entityWithoutMarketingSpend(DEFAULT_TOTAL_REVENUE));

        mockFinancialMetricHistory(input);
        mockFinancialMetric(input, mockedMetric);

        var response = processor.process(input);

        verifyNewEntryToMetricHistory();

        assertMetricTotalRevenueValue(input, DEFAULT_TOTAL_REVENUE, response);
        assertEquals(response.getTotalRevenue(), response.getRevenueAtTimeOfMarketingSpend());
        assertEquals(input.getMarketingSpend(), response.getMarketingSpend());
        assertEquals(DEFAULT_FINANCIAL_METRIC_ITEM_DAY, response.getMarketingSpendDay());
        assertNull(response.getPaybackPeriod());
        assertUnchangableFields(mockedMetric.get(), response);

    }

    // newInputAndAlreadyHasMarketingSpendAndNotBreakEven
    @Test
    void validStateNewInputAndAlreadyHasMarketingSpendAndNotBreakEven() {
        var input = customFinancialMetricItem(BigDecimal.valueOf(100));
        var mockedMetric = Optional.of(entityWithMarketingSpend(
            DEFAULT_TOTAL_REVENUE,
            DEFAULT_MARKETING_SPEND,
            DEFAULT_MARKETING_SPEND_DAY,
            DEFAULT_REVENUE_AT_TIME_OF_MARKETING_SPEND
        ));

        mockFinancialMetricHistory(input);
        mockFinancialMetric(input, mockedMetric);

        var response = processor.process(input);

        verifyNewEntryToMetricHistory();

        assertMetricTotalRevenueValue(input, DEFAULT_TOTAL_REVENUE, response);
        assertEquals(DEFAULT_REVENUE_AT_TIME_OF_MARKETING_SPEND, response.getRevenueAtTimeOfMarketingSpend());
        assertEquals(DEFAULT_MARKETING_SPEND, response.getMarketingSpend());
        assertEquals(DEFAULT_MARKETING_SPEND_DAY, response.getMarketingSpendDay());
        assertNull(response.getPaybackPeriod());
        assertUnchangableFields(mockedMetric.get(), response);
    }
    // newInputAndAlreadyHasMarketingSpendAndGotBreakEven
    @Test
    void validStateNewInputAndAlreadyHasMarketingSpendAndGotBreakEven() {
        var input = customFinancialMetricItem(BigDecimal.valueOf(10000));
        var mockedMetric = Optional.of(entityWithMarketingSpend(
                DEFAULT_TOTAL_REVENUE,
                DEFAULT_MARKETING_SPEND,
                DEFAULT_MARKETING_SPEND_DAY,
                DEFAULT_REVENUE_AT_TIME_OF_MARKETING_SPEND
        ));

        mockFinancialMetricHistory(input);
        mockFinancialMetric(input, mockedMetric);

        var response = processor.process(input);

        verifyNewEntryToMetricHistory();

        assertMetricTotalRevenueValue(input, DEFAULT_TOTAL_REVENUE, response);
        assertEquals(DEFAULT_REVENUE_AT_TIME_OF_MARKETING_SPEND, response.getRevenueAtTimeOfMarketingSpend());
        assertEquals(DEFAULT_MARKETING_SPEND, response.getMarketingSpend());
        assertEquals(DEFAULT_MARKETING_SPEND_DAY, response.getMarketingSpendDay());
        assertEquals(DEFAULT_FINANCIAL_METRIC_ITEM_DAY-DEFAULT_MARKETING_SPEND_DAY, response.getPaybackPeriod());
        assertUnchangableFields(mockedMetric.get(), response);
    }
    // newInputAndAlreadyHasMarketingSpendAndAlreadyHasBreakEven
    @Test
    void validStateNewInputAndAlreadyHasMarketingSpendAndAlreadyHasBreakEven() {
        var totalRevenue = BigDecimal.valueOf(300000);
        var revenueAtTimeOfMarketingSpend = BigDecimal.valueOf(1000);
        var marketingSpend = BigDecimal.valueOf(10000);
        var marketingSpendDay = 1;
        var input = customFinancialMetricItem(BigDecimal.valueOf(10000));
        var mockedMetric = Optional.of(entityWithBreakEven(
                totalRevenue, marketingSpend, marketingSpendDay,
                revenueAtTimeOfMarketingSpend, PAYBACK_PERIOD));

        mockFinancialMetricHistory(input);
        mockFinancialMetric(input, mockedMetric);

        var response = processor.process(input);

        verifyNewEntryToMetricHistory();

        assertMetricTotalRevenueValue(input, totalRevenue, response);
        assertEquals(revenueAtTimeOfMarketingSpend, response.getRevenueAtTimeOfMarketingSpend());
        assertEquals(marketingSpend, response.getMarketingSpend());
        assertEquals(marketingSpendDay, response.getMarketingSpendDay());
        assertEquals(PAYBACK_PERIOD, response.getPaybackPeriod());
        assertUnchangableFields(mockedMetric.get(), response);
    }

    private static void assertMetricTotalRevenueValue(FinancialMetricItem input, BigDecimal mockedMetricInitialTotalRevenue, FinancialMetricEntity response) {
        var expectedTotalRevenue = mockedMetricInitialTotalRevenue.add(input.getRevenue());

        assertTrue(expectedTotalRevenue.compareTo(response.getTotalRevenue())==0);
    }

    private void verifyNewEntryToMetricHistory() {
        verify(historyRepository, times(1)).save(any());
    }

    private static void assertUnchangableFields(FinancialMetricEntity mockedMetric, FinancialMetricEntity response) {
        assertEquals(mockedMetric.getId(), response.getId());
        assertEquals(mockedMetric.getYearMonth(), response.getYearMonth());
        assertEquals(mockedMetric.getAppName(), response.getAppName());
        assertEquals(mockedMetric.getCompanyId(), response.getCompanyId());
        assertNull(response.getLtvCacRatio());
        assertNull(response.getRiskScore());
        assertNull(response.getRiskRating());
        assertFalse(response.getIsEvaluationFinished());
        assertFalse(response.getIsAlreadyExported());
    }

    private void mockFinancialMetricHistory(FinancialMetricItem input) {
        Optional<FinancialMetricHistoryEntity> mockedHistory = Optional.empty();

        when(historyRepository.findByDateAndAppNameAndCompanyId(
                eq(input.getDate()), eq(input.getAppName()), eq(input.getCompanyId()))
        ).thenReturn(mockedHistory);
    }

    private void mockFinancialMetric(FinancialMetricItem input, Optional<FinancialMetricEntity> mockedMetric) {
        when(metricRepository.findByAppNameAndCompanyId(input.getAppName(), input.getCompanyId())).thenReturn(mockedMetric);
    }
}
