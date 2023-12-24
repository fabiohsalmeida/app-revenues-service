package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.item.FinancialMetricItem;
import lombok.SneakyThrows;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class FinancialMetricItemWriter implements ItemWriter<FinancialMetricItem> {

    @Override
    @SneakyThrows
    public void write(Chunk<? extends FinancialMetricItem> chunk) {
        List<FinancialMetricItem> items = (List<FinancialMetricItem>) chunk.getItems();

        items.stream().forEach(i -> System.out.println(i.getDate()+","+i.getAppName()+","+i.getCompanyId()));
    }
}
