package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.domain.entity.FinancialMetricEntity;
import com.fhsa.apprevenues.domain.item.EvaluatedRiskScoreAppCompanyItem;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
        FinancialMetricEntity input = readyToBeExportedEntity();
        Optional<CompanyEntity> mockedCompanyEntity = Optional.of(defaultCompanyEntity());

        when(companyRepository.findById(eq(input.getCompanyId()))).thenReturn(mockedCompanyEntity);

        EvaluatedRiskScoreAppCompanyItem response = processor.process(input);

        assertEquals(input.getCompanyId(), response.getCompanyId());
        assertEquals(mockedCompanyEntity.get().getName(), response.getCompanyName());
        assertEquals(input.getAppName(), response.getAppName());
        assertEquals(input.getRiskScore().intValue(), response.getRiskScore());
        assertEquals(input.getRiskRating(), response.getRiskRating());
    }
}
