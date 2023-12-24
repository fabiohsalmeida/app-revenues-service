package com.fhsa.apprevenues.mother;

import com.fhsa.apprevenues.domain.item.CompanyItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CompanyItemMother {

    public static CompanyItem defaultCompanyItem() {
        return CompanyItem.builder()
            .id(100)
            .name("Kutch Tropical")
            .countryCode("US")
            .build();
    }

    public static CompanyItem customCompanyItem(Integer id, String name, String countryCode) {
        return CompanyItem.builder()
                .id(id)
                .name(name)
                .countryCode(countryCode)
                .build();
    }
}
