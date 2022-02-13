/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.utils;

import java.util.List;

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
}
