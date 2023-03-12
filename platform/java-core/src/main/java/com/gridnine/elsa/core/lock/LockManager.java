/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.lock;

import com.gridnine.elsa.core.model.common.RunnableWithException;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public interface LockManager {
    <T> T withLock(Object obj, long tryTime, TimeUnit timeUnit, Callable<T> func);

    default <T> T withLock(Object obj,  Callable<T> func) {
        return withLock(obj, 1, TimeUnit.MINUTES, func);
    }

    default void withLock(Object obj,  RunnableWithException func) {
        withLock(obj, 1, TimeUnit.MINUTES, () -> {func.run(); return null;});
    }

}
