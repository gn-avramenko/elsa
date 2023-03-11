/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class TextUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isBlank(String str) {
        return str == null || isStringBlank(str);
    }

    public static <T> String join(Collection<T> values, String separator){
        var sb = new StringBuilder();
        for(T value: values){
            if(sb.length() > 0){
                sb.append(",");
            }
            sb.append(value != null? value.toString(): "");
        }
        return sb.toString();
    }

    private static boolean isStringBlank(final CharSequence cs) {
        final int strLen = cs.length();
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getExceptionStackTrace(Throwable t) {
        if (t == null) {
            return null;
        }
        var sb = new StringBuilder(t.getStackTrace().length * 100);
        printError(t, t.toString(), sb);
        return sb.toString();
    }

    private static void printError(Throwable t, String header,
                                   StringBuilder sb) {
        if (t == null) {
            return;
        }
        var nl = System.getProperty("line.separator");
        if (!isBlank(header)) {
            sb.append(nl).append(header).append(nl).append(nl);
        }
        for (var element : t.getStackTrace()) {
            printStackTraceElement(element, sb).append(nl);
        }
        var next = t.getCause();
        printError(next, "Caused by %s".formatted(next), sb);
        if (t instanceof SQLException) {
            next = ((SQLException) t).getNextException();
            printError(next, "Next exception: %s".formatted(next), sb);
        } else if (t instanceof InvocationTargetException tg) {
            next = tg.getTargetException();
            printError(next, "Target exception: %s".formatted(next), sb);
        }
    }

    private static StringBuilder printStackTraceElement(StackTraceElement ste, StringBuilder sb) {
        sb.append("%s.%s(".formatted(ste.getClassName(), ste.getMethodName()));
        if (ste.isNativeMethod()) {
            sb.append("Native Method");
        } else if (ste.getFileName() != null) {
            sb.append("%s%s".formatted(ste.getFileName(), ste.getLineNumber() > 0 ? ste.getLineNumber() : ""));
        }
        sb.append(")");
        return sb;
    }

}
