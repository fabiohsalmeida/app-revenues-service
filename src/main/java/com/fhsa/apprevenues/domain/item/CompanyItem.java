package com.fhsa.apprevenues.domain.item;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyItem {

    private Integer id;
    private String name;
    private String countryCode;
}
