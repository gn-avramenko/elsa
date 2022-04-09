/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class TextUtils {

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static boolean isBlank(String str){
        return str == null || StringUtils.isBlank(str);
    }
}
