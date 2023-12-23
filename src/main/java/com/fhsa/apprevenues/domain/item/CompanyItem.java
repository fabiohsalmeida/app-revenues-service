package com.fhsa.apprevenues.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyItem {

    private Integer id;
    private String name;
    private String countryCode;

    @Override
    public String toString() {
        String toStringFormat = "%s{%s: %d, %s: %s, %s %s}";

        return String.format(
            toStringFormat,
            this.getName(),
            "id", id,
            "name", name,
            "countryCode", countryCode
        );
    }
}
