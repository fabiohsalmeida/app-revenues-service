package com.fhsa.apprevenues.batch;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.item.CompanyItem;
import com.fhsa.apprevenues.domain.item.EvaluatedRiskScoreAppCompanyItem;
import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final DataSource dataSource;

    @Bean("evaluateAppCompanies")
    public Job evaluateAppCompanies(
        JobRepository jobRepository,
        Step processNewCompanies
    ) {
        String jobName = "Process new companies";

        return new JobBuilder(jobName, jobRepository)
            .start(processNewCompanies)
            .build();
    }

    @Bean("evaluateFinancialMetricsAndAppCreditRisks")
    public Job evaluateFinancialMetricsAndAppCreditRisks(
            JobRepository jobRepository,
            Step processFinancialMetrics,
            Step evaluateCreditRisk,
            Step exportUnprocessedMonths
    ) {
        String jobName = "Process new financial metrics and evaluate app credit risks";

        return new JobBuilder(jobName, jobRepository)
                .start(processFinancialMetrics)
                .next(evaluateCreditRisk)
                .next(exportUnprocessedMonths)
                .build();
    }

    @Bean
    public Step processNewCompanies(
        ItemReader<CompanyItem> reader,
        ItemProcessor<CompanyItem, CompanyEntity> processor,
        ItemWriter<CompanyEntity> writer,
        PlatformTransactionManager transactionManager,
        JobRepository jobRepository
    ) {
        String name = "Read from CSV and check if there's new companies to be processed";

        return new StepBuilder(name, jobRepository).<CompanyItem, CompanyEntity>
                chunk(completionPolicy(10), batchTransactionManager())
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(10)
                .skip(FlatFileParseException.class)
                .build();
    }

    @Bean
    public Step processFinancialMetrics(
        ItemReader<FinancialMetricItem> reader,
        ItemProcessor<FinancialMetricItem, FinancialMetricEntity> processor,
        ItemWriter<FinancialMetricEntity> writer,
        PlatformTransactionManager transactionManager,
        JobRepository jobRepository
    ) {
        String name = "Read from CSV and update financial metrics to database";

        return new StepBuilder(name, jobRepository).<FinancialMetricItem, FinancialMetricEntity>
                chunk(completionPolicy(1), batchTransactionManager())
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(10)
                .skip(FlatFileParseException.class)
                .build();
    }

    @Bean
    public Step evaluateCreditRisk(
        @Qualifier("repositoryFinancialMetricEntityReader") ItemReader<FinancialMetricEntity> reader,
        ItemProcessor<FinancialMetricEntity, FinancialMetricEntity> processor,
        ItemWriter<FinancialMetricEntity> writer,
        PlatformTransactionManager transactionManager,
        JobRepository jobRepository
    ) {
        String name = "Read from database non evaluated financial metrics and evaluate them";

        return new StepBuilder(name, jobRepository).<FinancialMetricEntity, FinancialMetricEntity>
                chunk(completionPolicy(20), batchTransactionManager())
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .build();
    }

    @Bean
    public Step exportUnprocessedMonths(
            @Qualifier("repositoryToBeExportedMetricsReader") ItemReader<FinancialMetricEntity> reader,
            ItemProcessor<FinancialMetricEntity, EvaluatedRiskScoreAppCompanyItem> processor,
            ItemWriter<EvaluatedRiskScoreAppCompanyItem> writer,
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository
    ) {
        String name = "Check if there months to be processed, if so process it";

        return new StepBuilder(name, jobRepository).<FinancialMetricEntity, EvaluatedRiskScoreAppCompanyItem>
                chunk(1, batchTransactionManager())
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .build();
    }

    public CompletionPolicy completionPolicy(Integer chunkSize) {
        CompositeCompletionPolicy policy = new CompositeCompletionPolicy();

        policy.setPolicies(
            new CompletionPolicy[] {
                new TimeoutTerminationPolicy(1000),
                new SimpleCompletionPolicy(chunkSize)}
        );

        return policy;
    }

    @Bean("transactionManager")
    public PlatformTransactionManager batchTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setDataSource(dataSource);

        return transactionManager;
    }
}
