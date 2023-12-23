package com.fhsa.apprevenues.config;

import com.fhsa.apprevenues.processor.CompanyItemProcessor;
import com.fhsa.apprevenues.writer.CompanyItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepScopedConfiguration {

    @Bean
    public CompanyItemProcessor companyItemProcessor() {
        return new CompanyItemProcessor();
    }

    @Bean
    public CompanyItemWriter companyItemWriter() {
        return new CompanyItemWriter();
    }
}
