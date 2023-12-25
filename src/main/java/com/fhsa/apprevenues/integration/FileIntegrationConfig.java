package com.fhsa.apprevenues.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.time.Duration;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class FileIntegrationConfig {

    @Qualifier("evaluateAppCompanies")
    private final Job evaluateAppCompanies;
    @Qualifier("evaluateFinancialMetricsAndAppCreditRisks")
    private final Job evaluateFinancialMetricsAndAppCreditRisks;
    private final JobRepository jobRepository;

    @Value("${input.company.directory}")
    private String inputCompanyDirectory;
    @Value("${input.metric.directory}")
    private String inputMetricDirectory;

    @Bean("companyJobIntegrationFlow")
    public IntegrationFlow companyJobIntegrationFlow() {
        return IntegrationFlow.from(
            companyFileReadingMessageSource(),
            sourcePolling -> sourcePolling.poller(Pollers.fixedDelay(Duration.ofSeconds(5)).maxMessagesPerPoll(1)))
            .channel(companyFileInDirectChannel())
            .handle(fileRenameProcessingHandler())
            .transform(companyFileMessageToJobRequest())
            .handle(companyJobLaunchingGateway())
            .channel(new NullChannel())
            .log()
            .get();
    }

    @Bean("companyFileReadingMessageSource")
    public FileReadingMessageSource companyFileReadingMessageSource() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();

        messageSource.setDirectory(new File(inputCompanyDirectory));
        messageSource.setFilter(new SimplePatternFileListFilter("*.csv"));

        return messageSource;
    }

    @Bean("companyFileInDirectChannel")
    public DirectChannel companyFileInDirectChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageHandler fileRenameProcessingHandler() {
        FileWritingMessageHandler fileWritingMessage = new FileWritingMessageHandler(new File(inputCompanyDirectory));

        fileWritingMessage.setFileExistsMode(FileExistsMode.REPLACE);
        fileWritingMessage.setDeleteSourceFiles(Boolean.TRUE);
        fileWritingMessage.setRequiresReply(Boolean.FALSE);
        fileWritingMessage.setFileNameGenerator(fileNameGenerator());

        return fileWritingMessage;
    }

    @Bean("companyFileMessageToJobRequest")
    public FileMessageToJobRequest companyFileMessageToJobRequest() {
        FileMessageToJobRequest transformer = new FileMessageToJobRequest();

        transformer.setJob(evaluateAppCompanies);

        return transformer;
    }

    @Bean("companyJobLaunchingGateway")
    public JobLaunchingGateway companyJobLaunchingGateway() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();

        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SyncTaskExecutor());

        return new JobLaunchingGateway(jobLauncher);
    }

    @Bean("metricJobIntegrationFlow")
    public IntegrationFlow metricJobIntegrationFlow() {
        return IntegrationFlow.from(
                        metricFileReadingMessageSource(),
                        sourcePolling -> sourcePolling.poller(Pollers.fixedDelay(Duration.ofSeconds(5)).maxMessagesPerPoll(1)))
                .channel(metricFileInDirectChannel())
                .handle(metricFileRenameProcessingHandler())
                .transform(metricFileMessageToJobRequest())
                .handle(metricJobLaunchingGateway())
                .channel(new NullChannel())
                .log()
                .get();
    }

    @Bean("metricFileReadingMessageSource")
    public FileReadingMessageSource metricFileReadingMessageSource() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();

        messageSource.setDirectory(new File(inputMetricDirectory));
        messageSource.setFilter(new SimplePatternFileListFilter("*.csv"));

        return messageSource;
    }

    @Bean("metricFileInDirectChannel")
    public DirectChannel metricFileInDirectChannel() {
        return new DirectChannel();
    }

    @Bean
    public DefaultFileNameGenerator fileNameGenerator() {
        DefaultFileNameGenerator fileNameGenerator = new DefaultFileNameGenerator();

        fileNameGenerator.setExpression("payload.name + '.processing'");

        return fileNameGenerator;
    }

    @Bean("metricFileRenameProcessingHandler")
    public MessageHandler metricFileRenameProcessingHandler() {
        FileWritingMessageHandler fileWritingMessage = new FileWritingMessageHandler(new File(inputMetricDirectory));

        fileWritingMessage.setFileExistsMode(FileExistsMode.REPLACE);
        fileWritingMessage.setDeleteSourceFiles(Boolean.TRUE);
        fileWritingMessage.setRequiresReply(Boolean.FALSE);
        fileWritingMessage.setFileNameGenerator(fileNameGenerator());

        return fileWritingMessage;
    }

    @Bean("metricFileMessageToJobRequest")
    public FileMessageToJobRequest metricFileMessageToJobRequest() {
        FileMessageToJobRequest transformer = new FileMessageToJobRequest();

        transformer.setJob(evaluateFinancialMetricsAndAppCreditRisks);

        return transformer;
    }

    @Bean("metricJobLaunchingGateway")
    public JobLaunchingGateway metricJobLaunchingGateway() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();

        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SyncTaskExecutor());

        return new JobLaunchingGateway(jobLauncher);
    }
}
