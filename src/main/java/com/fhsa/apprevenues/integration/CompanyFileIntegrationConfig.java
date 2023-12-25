package com.fhsa.apprevenues.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
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
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class CompanyFileIntegrationConfig {

    private final Job evaluateAppCreditRisks;
    private final JobRepository jobRepository;

    @Value("${input.company.directory}")
    private String inputCompanyDirectory;

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlow.from(
            fileReadingMessageSource(),
            sourcePolling -> sourcePolling.poller(Pollers.fixedDelay(Duration.ofSeconds(5)).maxMessagesPerPoll(1)))
            .channel(fileIn())
            .handle(fileRenameProcessingHandler())
            .transform(fileMessageToJobRequest())
            .handle(jobLaunchingGateway())
            .channel(new NullChannel())
            .log()
            .get();
    }

    @Bean
    public FileReadingMessageSource fileReadingMessageSource() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();

        messageSource.setDirectory(new File(inputCompanyDirectory));
        messageSource.setFilter(new SimplePatternFileListFilter("*.csv"));

        return messageSource;
    }

    @Bean
    public DirectChannel fileIn() {
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

    @Bean
    public DefaultFileNameGenerator fileNameGenerator() {
        DefaultFileNameGenerator fileNameGenerator = new DefaultFileNameGenerator();

        fileNameGenerator.setExpression("payload.name + '.processing'");

        return fileNameGenerator;
    }

    @Bean
    public FileMessageToJobRequest fileMessageToJobRequest() {
        FileMessageToJobRequest transformer = new FileMessageToJobRequest();

        transformer.setJob(evaluateAppCreditRisks);

        return transformer;
    }

    @Bean
    public JobLaunchingGateway jobLaunchingGateway() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();

        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SyncTaskExecutor());

        return new JobLaunchingGateway(jobLauncher);
    }
}
