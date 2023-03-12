/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.utils;

import java.util.Locale;
import java.util.Map;

public class LocaleUtils {
    public static final Locale ruLocale =new Locale("ru");

    private final static ThreadLocal<Locale> currentLocale = new ThreadLocal<>();

    public static Locale getLocale(String language, String countryCode){
        if("ru".equals(language)){
            return ruLocale;
        }
        if("en".equals(language)){
            return Locale.ENGLISH;
        }
        return new Locale(language, countryCode);
    }

    public static Locale getLocale(String language){
        return getLocale(language, "");
    }


    public static void setCurrentLocale(Locale locale){
        currentLocale.set(locale);
    }

    public static Locale getCurrentLocale(){
        var locale = currentLocale.get();
        return locale == null ? ruLocale : locale;
    }

    public static void resetCurrentLocale(){
        currentLocale.set(null);
    }

    public static String getLocalizedName(Map<Locale, String> localizations, Locale loc, String defaultValue){
        var result = localizations.get(loc == null? getCurrentLocale(): loc);
        if(result == null){
            result = localizations.get(ruLocale);
        }
        if(result == null && !localizations.isEmpty()){
            result = localizations.values().iterator().next();
        }
        return result == null? defaultValue: result;
    }
}
