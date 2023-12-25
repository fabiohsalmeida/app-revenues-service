package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class CompanyItemWriter implements ItemWriter<CompanyEntity> {

    private final CompanyRepository companyRepository;

    @Override
    @SneakyThrows
    public void write(Chunk<? extends CompanyEntity> chunk) {
        List<CompanyEntity> items = (List<CompanyEntity>) chunk.getItems();

        companyRepository.saveAll(items);
    }
}
