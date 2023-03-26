/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

abstract class BaseStorageOperationParameters<T extends BaseStorageOperationParameters<T>> {
    private boolean skipInterceptors;

    public T skipInterceptors(boolean value){
        skipInterceptors =value;
        return (T) this;
    }
    public boolean isSkipInterceptors() {
        return skipInterceptors;
    }
}
