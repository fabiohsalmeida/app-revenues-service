package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

import static com.fhsa.apprevenues.mother.CompanyEntityMother.defaultCompanyEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CompanyItemWriterTest {

    @InjectMocks
    private CompanyItemWriter writer;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    void validStateWrite() {
        writer.write(Chunk.of(defaultCompanyEntity()));

        verify(companyRepository, times(1)).saveAll(any());
    }
}
