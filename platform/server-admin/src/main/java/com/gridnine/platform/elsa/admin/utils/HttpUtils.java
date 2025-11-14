package com.gridnine.platform.elsa.admin.utils;

import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class HttpUtils {
    public static String getQueryParameter(HttpServletRequest request, String name) {
        var queryString = request.getQueryString();
        if(TextUtils.isBlank(queryString)) {
            return null;
        }
        queryString = queryString.substring(queryString.indexOf("?")+1);
        var tokens = queryString.split("\\&");
        if(tokens.length > 0) {
            var value = Arrays.stream(tokens).filter(it -> it.startsWith("%s=".formatted(name))).findFirst().orElse(null);
            if(value != null) {
                return value.substring(name.length()+1);
            }
        }
        return null;
    }
}
