package com.fhsa.apprevenues.domain.enums;

import lombok.Getter;

@Getter
public enum RiskRatingEnum {
    UNDOUBTED("Undoubted"),
    LOW("Low"),
    MODERATE("Moderate"),
    CAUTIONARY("Cautionary"),
    UNSATISFACTORY("Unsatisfactory"),
    UNACCEPTABLE("Unacceptable");

    private String value;

    RiskRatingEnum(String value) {
        this.value = value;
    }
}
