/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.utils;

import java.util.concurrent.Callable;

public class ExceptionUtils {
    public static void wrapException(RunnableWithException body){
        try {
            body.run();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static<T> T wrapException(Callable<T> body){
        try {
            return body.call();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
