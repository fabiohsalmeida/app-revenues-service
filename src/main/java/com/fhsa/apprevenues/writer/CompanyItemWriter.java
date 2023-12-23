package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CompanyItemWriter implements ItemWriter<CompanyEntity> {

    private final CompanyRepository companyRepository;

    @Override
    public void write(Chunk<? extends CompanyEntity> chunk) throws Exception {
        List<CompanyEntity> items = (List<CompanyEntity>) chunk.getItems();

        companyRepository.saveAll(items);
    }
}