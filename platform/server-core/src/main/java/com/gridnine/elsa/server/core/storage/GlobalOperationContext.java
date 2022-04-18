/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.utils.Lazy;
import com.gridnine.elsa.server.core.storage.transaction.ElsaTransactionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class GlobalOperationContext {

    private final Lazy<BaseIdentity> oldObjectProvider;

    private final Map<String, Object> parameters = new HashMap<>();

    private final BaseIdentity newObject;

    private final ElsaTransactionContext context;

    public GlobalOperationContext(BaseIdentity newObject, Callable<BaseIdentity> oldObjectFactory, ElsaTransactionContext context) {
        this.oldObjectProvider = new Lazy<>(oldObjectFactory);
        this.newObject = newObject;
        this.context = context;
    }

    public GlobalOperationContext(BaseIdentity newObject, BaseIdentity oldObject, ElsaTransactionContext context) {
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

    public ElsaTransactionContext getContext() {
        return context;
    }
}
