package com.fhsa.apprevenues.writer;

import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

import static com.fhsa.apprevenues.mother.FinancialMetricEntityMother.readyToBeExportedEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FinancialMetricEntityWriterTest {

    @InjectMocks
    private FinancialMetricEntityWriter writer;

    @Mock
    private FinancialMetricRepository metricRepository;

    @Test
    public void validStateWrite() {
        writer.write(Chunk.of(readyToBeExportedEntity()));

        verify(metricRepository, times(1)).saveAll(any());
    }
}
