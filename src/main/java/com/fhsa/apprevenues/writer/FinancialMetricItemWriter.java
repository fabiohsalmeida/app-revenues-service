package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import lombok.SneakyThrows;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class FinancialMetricItemWriter implements ItemWriter<FinancialMetricEntity> {

    @Override
    @SneakyThrows
    public void write(Chunk<? extends FinancialMetricEntity> chunk) {
        List<FinancialMetricEntity> items = (List<FinancialMetricEntity>) chunk.getItems();

        items.stream().forEach(i -> System.out.println(i.getDate()+","+i.getAppName()+","+i.getCompanyId()));
    }
}
