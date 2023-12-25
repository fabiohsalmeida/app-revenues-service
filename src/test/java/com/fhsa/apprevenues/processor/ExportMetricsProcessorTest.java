package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.exception.CompanyNotFoundException;
import com.fhsa.apprevenues.repository.CompanyRepository;
import com.fhsa.apprevenues.repository.FinancialMetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fhsa.apprevenues.mother.CompanyEntityMother.defaultCompanyEntity;
import static com.fhsa.apprevenues.mother.FinancialMetricEntityMother.readyToBeExportedEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExportMetricsProcessorTest {

    @InjectMocks
    private ExportMetricsProcessor processor;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private FinancialMetricRepository metricRepository;

    @Test
    void validStateProcess() {
        var input = readyToBeExportedEntity();
        var mockedCompanyEntity = Optional.of(defaultCompanyEntity());

        when(companyRepository.findById(eq(input.getCompanyId()))).thenReturn(mockedCompanyEntity);

        var response = processor.process(input);

        verify(metricRepository, times(1)).save(any());

        assertEquals(input.getCompanyId(), response.getCompanyId());
        assertEquals(mockedCompanyEntity.get().getName(), response.getCompanyName());
        assertEquals(input.getAppName(), response.getAppName());
        assertEquals(input.getRiskScore().intValue(), response.getRiskScore());
        assertEquals(input.getRiskRating(), response.getRiskRating());
    }

    @Test
    void invalidStateCompanyNotFoundException() {
        var input = readyToBeExportedEntity();

        when(companyRepository.findById(eq(input.getCompanyId()))).thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(
            CompanyNotFoundException.class, () -> { processor.process(input); }
        );

        assertEquals("Company "+input.getCompanyId()+" not found.", exception.getMessage());
    }
}
