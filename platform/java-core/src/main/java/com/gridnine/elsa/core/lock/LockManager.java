/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.lock;

import com.gridnine.elsa.core.model.common.RunnableWithException;
import com.gridnine.elsa.meta.config.Environment;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public abstract class LockManager {
    public abstract <T> T withLock(Object obj, long tryTime, TimeUnit timeUnit, Callable<T> func);

    public <T> T withLock(Object obj,  Callable<T> func) {
        return withLock(obj, 1, TimeUnit.MINUTES, func);
    }

    public void withLock(Object obj,  RunnableWithException func) {
        withLock(obj, 1, TimeUnit.MINUTES, () -> {func.run(); return null;});
    }

    public static LockManager get(){
        return Environment.getPublished(LockManager.class);
    }

}
