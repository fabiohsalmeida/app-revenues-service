package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.domain.item.CompanyItem;
import com.fhsa.apprevenues.mother.CompanyEntityMother;
import com.fhsa.apprevenues.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fhsa.apprevenues.mother.CompanyEntityMother.defaultCompanyEntity;
import static com.fhsa.apprevenues.mother.CompanyItemMother.customCompanyItem;
import static com.fhsa.apprevenues.mother.CompanyItemMother.defaultCompanyItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyItemProcessorTest {

    @InjectMocks
    public CompanyItemProcessor processor;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    void invalidStateItemAlreadyInUse() {
        Optional<CompanyEntity> mockedCompanyEntity = Optional.of(defaultCompanyEntity());
        CompanyItem inputCompanyItem = defaultCompanyItem();

        when(companyRepository.findById(eq(inputCompanyItem.getId()))).thenReturn(mockedCompanyEntity);

        CompanyEntity response = processor.process(inputCompanyItem);

        assertNull(response);
    }

    @Test
    void validStateItemNotInUse() {
        Optional<CompanyEntity> mockedEntity = Optional.empty();
        CompanyItem inputCompanyItem = defaultCompanyItem();

        when(companyRepository.findById(eq(inputCompanyItem.getId()))).thenReturn(mockedEntity);

        CompanyEntity response = processor.process(inputCompanyItem);

        assertNotNull(response);
        assertEquals(inputCompanyItem.getId(), response.getId());
        assertEquals(inputCompanyItem.getName(), response.getName());
        assertEquals(inputCompanyItem.getCountryCode(), response.getCountryCode());
    }

    @Test
    void invalidStateDifferentItemInSameId() {
        Optional<CompanyEntity> mockedCompanyEntity = Optional.of(defaultCompanyEntity());
        CompanyItem inputCompanyItem = customCompanyItem(
                mockedCompanyEntity.get().getId(),
                "Different Name",
                "AZ");

        when(companyRepository.findById(eq(inputCompanyItem.getId()))).thenReturn(mockedCompanyEntity);

        CompanyEntity response = processor.process(inputCompanyItem);

        assertNull(response);
    }
}
