/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.standard;

import com.gridnine.elsa.common.model.common.IdGenerator;
import com.gridnine.elsa.common.model.domain.BaseAsset;
import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.server.storage.OperationContext;
import com.gridnine.elsa.server.storage.StorageInterceptor;


public class IdUpdaterInterceptor implements StorageInterceptor {

    @Override
    public double getPriority() {
        return 0;
    }

    @Override
    public <A extends BaseAsset> void onSave(A asset, OperationContext<A> context) {
        if(asset.getId() == -1){
            asset.setId(IdGenerator.get().nextId());
        } else {
            IdGenerator.get().ensureIdRegistered(asset.getId());
        }
    }

    @Override
    public <D extends BaseDocument> void onSave(D doc, OperationContext<D> context) {
        if(doc.getId() == -1){
            doc.setId(IdGenerator.get().nextId());
        } else {
            IdGenerator.get().ensureIdRegistered(doc.getId());
        }
    }
}
