/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.core.model.common.BaseIdentity;
import com.gridnine.elsa.core.utils.Lazy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class LocalOperationContext<I extends BaseIdentity> {
    private final Lazy<I> oldObjectProvider;

    private final Map<String, Object> parameters = new HashMap<>();

    public LocalOperationContext(Callable<I> oldObjectFactory) {
        this.oldObjectProvider = new Lazy<>(oldObjectFactory);
    }

    public LocalOperationContext(I oldObject) {
        this.oldObjectProvider = new Lazy<>(oldObject);
    }

    public I getOldObject() {
        return oldObjectProvider.getObject();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}
