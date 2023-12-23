package com.fhsa.apprevenues.config;

import com.fhsa.apprevenues.domain.item.CompanyItem;
import com.fhsa.apprevenues.processor.CompanyItemProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    @Bean
    public Job processAppCompanies(Step step1, JobRepository jobRepository) {
        String jobName = "Process App Companies";

        return new JobBuilder(jobName, jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(
        ItemReader<CompanyItem> reader,
        ItemProcessor<CompanyItem, CompanyItem> processor,
        ItemWriter<CompanyItem> writer,
        PlatformTransactionManager transactionManager,
        DataSource dataSource,
        JobRepository jobRepository
    ) {
        String name = "Do what you have to do";

        return new StepBuilder(name, jobRepository).<CompanyItem, CompanyItem>
                chunk(completionPolicy(), batchTransactionManager(dataSource))
                .reader(reader)
                .processor(processor)
                .writer(writer)
//                .faultTolerant()
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public CompletionPolicy completionPolicy() {
        CompositeCompletionPolicy policy = new CompositeCompletionPolicy();

        policy.setPolicies(
            new CompletionPolicy[] {
                new TimeoutTerminationPolicy(1000),
                new SimpleCompletionPolicy(10)}
        );

        return policy;
    }

    @Bean
    public DataSourceTransactionManager batchTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


//    @Bean
//    public Job job(JobRepository jobRepository, Step step) {
//        return new JobBuilder("job", jobRepository)
//            .start(step)
//            .build();
//    }
//
//    @Bean
//    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("step", jobRepository)
//            .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
//                System.out.println("Hello world");
//
//                return RepeatStatus.FINISHED;
//            }, transactionManager)
//            .build();
//    }

}
