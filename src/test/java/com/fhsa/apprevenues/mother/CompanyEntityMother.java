package com.fhsa.apprevenues.mother;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import com.fhsa.apprevenues.domain.entity.CompanyEntity.CompanyEntityBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CompanyEntityMother {

    public static CompanyEntity defaultCompanyEntity() {
        return preset()
            .id(100)
            .build();
    }

    public static CompanyEntity customCompanyEntity(Integer companyId) {
        return preset()
            .id(companyId)
            .build();
    }

    private static CompanyEntityBuilder preset() {
        return CompanyEntity.builder()
            .name("Insane App")
            .countryCode("US");
    }
}
