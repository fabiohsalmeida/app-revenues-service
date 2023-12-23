package com.fhsa.apprevenues.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppRevenuesTestDataProviderUtils {

    public static String supplyValidAppCompaniesContent() {
        return "company_id,company_name,country_code\n" +
                "100,Kutch Games,US\n" +
                "101,SunTropical,US\n" +
                "102,Hunters,GB\n" +
                "103,Mind Flourish,US\n" +
                "104,Guard Games,DE\n" +
                "105,Cyberdroid Games,CA\n" +
                "106,Astrocraft,FR\n" +
                "107,Castlecore,FI\n" +
                "108,Chromarush,US\n" +
                "109,Fitness Champ Games,GB\n" +
                "110,Bluescape,US";
    }
}
