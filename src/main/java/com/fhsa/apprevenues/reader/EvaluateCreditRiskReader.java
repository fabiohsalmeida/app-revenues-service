package com.fhsa.apprevenues.reader;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class EvaluateCreditRiskReader {

    private final FinancialMetricRepository financialMetricRepository;

    @Bean
    public ItemReader<FinancialMetricEntity> reader() {
        RepositoryItemReader<FinancialMetricEntity> reader = new RepositoryItemReader<>();

        reader.setRepository(financialMetricRepository);
        reader.setMethodName("findByIsEvaluationFinished");
        reader.setArguments(Collections.singletonList(Boolean.FALSE));
        reader.setPageSize(20);
        reader.setSort(buildSort());

        return reader;
    }

    private Map<String, Sort.Direction> buildSort() {
        HashMap<String, Sort.Direction> sorts = new HashMap<>();

        sorts.put("id", Sort.Direction.ASC);

        return sorts;
    }
}
