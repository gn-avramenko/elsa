/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.utils.ExceptionUtils;

import java.util.concurrent.Callable;

public class CachedObjectProvider<T> {
    private final Callable<T> factory;
    private boolean initialized = false;
    private T cachedValue;
    public CachedObjectProvider(Callable<T> factory){
        this.factory = factory;
    }

    public CachedObjectProvider(T object){
        this.cachedValue = object;
        initialized = true;
        factory = null;
    }

    public T getObject(){
        if(!initialized){
            cachedValue = ExceptionUtils.wrapException(factory::call);
            initialized = true;
        }
        return cachedValue;
    }
}
