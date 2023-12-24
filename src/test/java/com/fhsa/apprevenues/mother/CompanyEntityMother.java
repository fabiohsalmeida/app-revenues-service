package com.fhsa.apprevenues.mother;

import com.fhsa.apprevenues.domain.entity.CompanyEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CompanyEntityMother {

    public static CompanyEntity defaultCompanyEntity() {
        return CompanyEntity.builder()
            .id(100)
            .name("Kutch Tropical")
            .countryCode("US")
            .build();
    }
}
