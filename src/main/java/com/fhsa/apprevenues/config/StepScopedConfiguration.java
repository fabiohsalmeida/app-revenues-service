package com.fhsa.apprevenues.config;

import com.fhsa.apprevenues.processor.CompanyItemProcessor;
import com.fhsa.apprevenues.reader.CompanyItemReader;
import com.fhsa.apprevenues.repository.CompanyRepository;
import com.fhsa.apprevenues.writer.CompanyItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StepScopedConfiguration {

    public final CompanyRepository companyRepository;

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
}
