package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.item.EvaluatedRiskScoreAppCompanyItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class RiskScoreAppCompanyExportWriter {

    @Bean
    public FlatFileItemWriter<EvaluatedRiskScoreAppCompanyItem> writer() {
        FlatFileItemWriter<EvaluatedRiskScoreAppCompanyItem> writer = new FlatFileItemWriter<>();

        writer.setResource(new FileSystemResource("output/app-credit-risk-ratings.csv"));
        writer.setAppendAllowed(true);
        writer.setLineAggregator(lineAggregator());
        writer.setHeaderCallback(headerCallback());

        return writer;
    }

    private FlatFileHeaderCallback headerCallback() {
        return writer -> writer.write("company_id,company_name,app_name,risk_score,risk_rating");
    }

    private LineAggregator<EvaluatedRiskScoreAppCompanyItem> lineAggregator() {
        DelimitedLineAggregator lineAggregator = new DelimitedLineAggregator<>();

        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor());

        return lineAggregator;
    }

    private FieldExtractor fieldExtractor() {
        BeanWrapperFieldExtractor fieldExtractor = new BeanWrapperFieldExtractor();

        fieldExtractor.setNames(getFieldNames());

        return fieldExtractor;
    }

    private String[] getFieldNames() {
        return new String[]{
            "companyId",
            "companyName",
            "appName",
            "riskScore",
            "riskRating"
        };
    }
}
