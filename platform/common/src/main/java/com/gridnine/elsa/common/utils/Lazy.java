/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.utils;

import java.util.concurrent.Callable;

public class Lazy<T> {
    private final Callable<T> factory;
    private boolean initialized = false;
    private T cachedValue;
    public Lazy(Callable<T> factory){
        this.factory = factory;
    }

    public Lazy(T object){
        this.cachedValue = object;
        initialized = true;
        factory = null;
    }

    public T getObject(){
        if(!initialized){
            cachedValue = ExceptionUtils.wrapException(factory);
            initialized = true;
        }
        return cachedValue;
    }
}
