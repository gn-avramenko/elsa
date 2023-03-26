/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.utils;

import com.gridnine.elsa.common.model.common.RunnableWithException;

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
