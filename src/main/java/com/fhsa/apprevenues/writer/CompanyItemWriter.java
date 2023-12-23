package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.item.CompanyItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class CompanyItemWriter implements ItemWriter<CompanyItem> {

    @Override
    public void write(Chunk<? extends CompanyItem> chunk) throws Exception {
        List<CompanyItem> items = (List<CompanyItem>) chunk.getItems();

        System.out.println("Chegou no writer");

        items.stream().forEach(i -> System.out.println(i));
    }
}
