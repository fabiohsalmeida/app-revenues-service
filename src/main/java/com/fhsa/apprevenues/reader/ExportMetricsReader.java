package com.fhsa.apprevenues.reader;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import static com.fhsa.apprevenues.util.ReaderUtils.buildDefaultSort;

@Configuration
@RequiredArgsConstructor
public class ExportMetricsReader {

    private final FinancialMetricRepository metricRepository;

    @Bean
    public ItemReader<FinancialMetricEntity> repositoryToBeExportedMetricsReader() {
        RepositoryItemReader<FinancialMetricEntity> reader = new RepositoryItemReader<>();

        reader.setName("toBeExportedMetricsReader");
        reader.setRepository(metricRepository);
        reader.setMethodName("findByIsAlreadyExported");
        reader.setArguments(Collections.singletonList(Boolean.FALSE));
        reader.setPageSize(20);
        reader.setSort(buildDefaultSort());

        return reader;
    }
}
