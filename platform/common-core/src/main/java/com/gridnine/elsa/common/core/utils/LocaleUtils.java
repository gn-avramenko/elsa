/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.utils;

import java.util.Locale;

public class LocaleUtils {
    private static final Locale ruLocale =new Locale("ru");

    public static Locale getLocale(String language, String countryCode){
        if("ru".equals(language)){
            return ruLocale;
        }
        if("en".equals(language)){
            return Locale.ENGLISH;
        }
        return new Locale(language, countryCode);
    }
}
