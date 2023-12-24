package com.fhsa.apprevenues.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ReaderUtils {

    public static Map<String, Sort.Direction> buildDefaultSort() {
        HashMap<String, Sort.Direction> sorts = new HashMap<>();

        sorts.put("id", Sort.Direction.ASC);

        return sorts;
    }
}
