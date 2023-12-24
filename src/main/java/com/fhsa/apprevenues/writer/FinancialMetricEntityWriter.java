package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class FinancialMetricEntityWriter implements ItemWriter<FinancialMetricEntity> {

    private final FinancialMetricRepository financialMetricRepository;

    @Override
    @SneakyThrows
    public void write(Chunk<? extends FinancialMetricEntity> chunk) {
        List<FinancialMetricEntity> items = (List<FinancialMetricEntity>) chunk.getItems();

        FinancialMetricEntity entity = items.stream().findFirst().get();

        financialMetricRepository.save(entity);
    }
}
