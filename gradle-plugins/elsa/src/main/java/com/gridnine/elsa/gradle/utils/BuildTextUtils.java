/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.utils;

import java.util.List;
import java.util.Locale;

public class BuildTextUtils {

    public static boolean isBlank(String text){
        return text == null || text.isBlank();
    }

    public static String joinToString(List<?> objects, String separator){
        var buf = new StringBuffer();
        objects.forEach(it ->{
            if(!buf.isEmpty()){
                buf.append(separator);
            }
            buf.append(it);
        });
        return buf.toString();
    }

    public static String capitalize(String value){
        return value.substring(0, 1).toUpperCase(Locale.ROOT)+value.substring(1);
    }
}
