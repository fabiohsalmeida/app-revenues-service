package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.domain.item.CompanyItem;
import org.springframework.batch.item.ItemProcessor;

public class CompanyItemProcessor implements ItemProcessor<CompanyItem, CompanyEntity> {

    @Override
    public CompanyEntity process(CompanyItem companyItem) throws Exception {
        return new CompanyEntity(
            companyItem.getId(),
            companyItem.getName(),
            companyItem.getCountryCode()
        );
    }
}
