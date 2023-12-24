package com.fhsa.apprevenues.batch;

import com.fhsa.apprevenues.processor.CompanyItemProcessor;
import com.fhsa.apprevenues.processor.EvaluateCreditRiskProcessor;
import com.fhsa.apprevenues.processor.FinancialMetricItemProcessor;
import com.fhsa.apprevenues.processor.ExportMetricsProcessor;
import com.fhsa.apprevenues.repository.CompanyRepository;
import com.fhsa.apprevenues.repository.FinancialMetricHistoryRepository;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import com.fhsa.apprevenues.writer.CompanyItemWriter;
import com.fhsa.apprevenues.writer.FinancialMetricEntityWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StepScopedConfiguration {

    private final CompanyRepository companyRepository;
    private final FinancialMetricRepository metricRepository;
    private final FinancialMetricHistoryRepository metricHistoryRepository;

    @Bean
    @StepScope
    public CompanyItemProcessor companyItemProcessor() {
        return new CompanyItemProcessor(companyRepository);
    }

    @Bean
    @StepScope
    public CompanyItemWriter companyItemWriter() {
        return new CompanyItemWriter(companyRepository);
    }

    @Bean
    @StepScope
    public FinancialMetricItemProcessor financialMetricItemProcessor() {
        return new FinancialMetricItemProcessor(
            metricRepository,
            metricHistoryRepository
        );
    }

    @Bean
    @StepScope
    public FinancialMetricEntityWriter financialMetricItemWriter() {
        return new FinancialMetricEntityWriter(
            metricRepository
        );
    }

    @Bean
    @StepScope
    public EvaluateCreditRiskProcessor evaluateCreditRiskProcessor() {
        return new EvaluateCreditRiskProcessor(
            metricRepository,
            metricHistoryRepository
        );
    }

    @Bean
    @StepScope
    public ExportMetricsProcessor financialMonthProcessor() {
        return new ExportMetricsProcessor(
            companyRepository,
            metricRepository
        );
    }
}
