package com.fhsa.apprevenues.domain.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EvaluatedRiskScoreAppCompanyItem {

    private Integer companyId;
    private String companyName;
    private String appName;
    private Integer riskScore;
    private String riskRating;
}
