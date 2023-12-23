package com.fhsa.apprevenues.config;

import com.fhsa.apprevenues.processor.CompanyItemProcessor;
import com.fhsa.apprevenues.repository.CompanyRepository;
import com.fhsa.apprevenues.writer.CompanyItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StepScopedConfiguration {

    public final CompanyRepository companyRepository;

    @Bean
    public CompanyItemProcessor companyItemProcessor() {
        return new CompanyItemProcessor();
    }

    @Bean
    public CompanyItemWriter companyItemWriter() {
        return new CompanyItemWriter(companyRepository);
    }
}
