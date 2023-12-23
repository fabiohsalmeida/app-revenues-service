package com.fhsa.apprevenues.processor;

import com.fhsa.apprevenues.domain.item.CompanyItem;
import org.springframework.batch.item.ItemProcessor;

public class CompanyItemProcessor implements ItemProcessor<CompanyItem, CompanyItem> {

    @Override
    public CompanyItem process(CompanyItem companyItem) throws Exception {
        System.out.println(companyItem.toString());

        return companyItem;
    }
}
