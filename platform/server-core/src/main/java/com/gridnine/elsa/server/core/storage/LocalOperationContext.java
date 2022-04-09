/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class LocalOperationContext<I extends BaseIdentity> {
    private final CachedObjectProvider<I> oldObjectProvider;

    private final Map<String, Object> parameters = new HashMap<>();

    public LocalOperationContext(Callable<I> oldObjectFactory) {
        this.oldObjectProvider = new CachedObjectProvider<>(oldObjectFactory);
    }

    public LocalOperationContext(I oldObject) {
        this.oldObjectProvider = new CachedObjectProvider<>(oldObject);
    }

    public I getOldObject() {
        return oldObjectProvider.getObject();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}
