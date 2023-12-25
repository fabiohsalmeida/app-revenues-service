package com.fhsa.apprevenues.reader;

import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class FinancialMetricItemReader {

    @Bean
    @StepScope
    public FlatFileItemReader<FinancialMetricItem> financialMetricItemFlatFileItemReader(
        @Value("#{jobParameters['input.file.name']}") String input,
        LineMapper<FinancialMetricItem> lineMapper
    ) {
        FlatFileItemReader<FinancialMetricItem> financialMetricsReader = new FlatFileItemReader<>();

        financialMetricsReader.setName("financialMetricsReader");
        financialMetricsReader.setLinesToSkip(1);
        financialMetricsReader.setResource(new FileSystemResource(input));
        financialMetricsReader.setLineMapper(lineMapper);

        return financialMetricsReader;
    }

    @Bean
    public DefaultLineMapper<FinancialMetricItem> financialMetricItemLineMapper(
        @Qualifier("financialMetricsTokenizer") LineTokenizer tokenizer,
        FieldSetMapper<FinancialMetricItem> fieldSetMapper
    ) {
        DefaultLineMapper<FinancialMetricItem> lineMapper = new DefaultLineMapper<>();

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean("financialMetricsTokenizer")
    public DelimitedLineTokenizer financialMetricsTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setDelimiter(",");
        tokenizer.setNames("date", "appName", "companyId", "revenue", "marketingSpend");

        return tokenizer;
    }

    @Bean
    public BeanWrapperFieldSetMapper<FinancialMetricItem> financialMetricItemBeanWrapperFieldSetMapper() {
        BeanWrapperFieldSetMapper<FinancialMetricItem> fieldSetMapper = new BeanWrapperFieldSetMapper<>();

        fieldSetMapper.setTargetType(FinancialMetricItem.class);

        return fieldSetMapper;
    }
}
