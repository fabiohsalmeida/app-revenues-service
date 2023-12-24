package com.fhsa.apprevenues.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class EvaluateUtils {

    public static boolean isBetween(BigDecimal value, Double higherOrEquals, Double less) {
        return isHigherThan(value, higherOrEquals) && isLessThan(value, less);
    }

    public static boolean isHigherThan(BigDecimal value, Double higherOrEquals) {
        return value.compareTo(BigDecimal.valueOf(higherOrEquals)) >= 0;
    }

    public static boolean isLessThan(BigDecimal value, Double less) {
        return value.compareTo(BigDecimal.valueOf(less)) < 0;
    }

    public static boolean isBetween(Integer value, Integer higherOrEquals, Integer less) {
        return isHigherThan(value, higherOrEquals) && isLessThan(value, less);
    }

    public static boolean isHigherThan(Integer value, Integer higherOrEquals) {
        return value >= higherOrEquals;
    }

    public static boolean isLessThan(Integer value, Integer less) {
        return value < less;
    }
}
