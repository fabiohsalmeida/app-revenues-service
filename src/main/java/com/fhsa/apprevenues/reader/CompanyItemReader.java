package com.fhsa.apprevenues.reader;

import com.fhsa.apprevenues.domain.item.CompanyItem;
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
import org.springframework.core.io.Resource;

@Configuration
public class CompanyItemReader {

    @Value("${input.company.directory}")
    private String inputCompanyDirectory;

//    @Value("classpath:input/app-companies.csv")
//    private Resource input;

    @Bean
    @StepScope
    public FlatFileItemReader<CompanyItem> companyItemFlatFileItemReader(
            @Value("#{jobParameters['input.file.name']}") String input,
        LineMapper<CompanyItem> lineMapper
    ) {
        FlatFileItemReader<CompanyItem> companyFileReader = new FlatFileItemReader<>();

        companyFileReader.setName("companyItemReader");
        companyFileReader.setLinesToSkip(1);
        companyFileReader.setResource(new FileSystemResource(input));
        companyFileReader.setLineMapper(lineMapper);

        return companyFileReader;
    }

    @Bean
    public DefaultLineMapper<CompanyItem> companyLineMapper(
            @Qualifier("companyLineTokenizer") LineTokenizer tokenizer,
            FieldSetMapper<CompanyItem> fieldSetMapper
    ) {
        DefaultLineMapper<CompanyItem> lineMapper = new DefaultLineMapper<>();

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public DelimitedLineTokenizer companyLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setDelimiter(",");
        tokenizer.setNames("id", "name", "countryCode");

        return tokenizer;
    }

    @Bean
    public BeanWrapperFieldSetMapper<CompanyItem> companyFieldSetMapper() {
        BeanWrapperFieldSetMapper<CompanyItem> fieldSetMapper = new BeanWrapperFieldSetMapper<>();

        fieldSetMapper.setTargetType(CompanyItem.class);

        return fieldSetMapper;
    }
}
