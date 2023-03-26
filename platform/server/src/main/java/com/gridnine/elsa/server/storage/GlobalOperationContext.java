/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.utils.Lazy;
import com.gridnine.elsa.server.storage.transaction.TransactionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class GlobalOperationContext {

    private final Lazy<BaseIdentity> oldObjectProvider;

    private final Map<String, Object> parameters = new HashMap<>();

    private final BaseIdentity newObject;

    private final TransactionContext context;

    public GlobalOperationContext(BaseIdentity newObject, Callable<BaseIdentity> oldObjectFactory, TransactionContext context) {
        this.oldObjectProvider = new Lazy<>(oldObjectFactory);
        this.newObject = newObject;
        this.context = context;
    }

    public GlobalOperationContext(BaseIdentity newObject, BaseIdentity oldObject, TransactionContext context) {
        this.oldObjectProvider = new Lazy<>(oldObject);
        this.newObject = newObject;
        this.context = context;
    }

    public BaseIdentity getOldObject() {
        return oldObjectProvider.getObject();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public BaseIdentity getNewObject() {
        return newObject;
    }

    public TransactionContext getContext() {
        return context;
    }
}
