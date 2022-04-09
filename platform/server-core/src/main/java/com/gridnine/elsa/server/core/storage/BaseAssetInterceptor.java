/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.utils.HasPriority;
import com.gridnine.elsa.common.core.utils.RunnableWithExceptionAndArgument;


@SuppressWarnings("unchecked")
public abstract class BaseAssetInterceptor<A> implements StorageAdvice {
    private final Class<A> cls;

    public BaseAssetInterceptor(Class<A> cls) {
        this.cls = cls;
    }

    protected abstract void onSaveAsset(A asset, RunnableWithExceptionAndArgument<A> callback) throws Exception;

    @Override
    public <B extends BaseAsset> void onSave(B asset, RunnableWithExceptionAndArgument<B> callback) throws Exception {
        if (asset.getClass().equals(cls)) {
            onSaveAsset((A) asset, (RunnableWithExceptionAndArgument<A>) callback);
        }
    }
}
