/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.search;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class SearchUtils {
    private static final DateTimeFormatter  dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss:SSS");
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String valueToString(Object value){
        if(value == null){
            return "null";
        }
        if(value instanceof Enum<?> e){
            return e.name();
        }
        if(value instanceof LocalDateTime ld){
            return dtf.format(ld);
        }
        if(value instanceof LocalDate ld){
            return df.format(ld);
        }
        return value.toString();
    }
}
