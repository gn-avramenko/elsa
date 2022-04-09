/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.standard;

import com.gridnine.elsa.common.core.model.common.IdGenerator;
import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.server.core.storage.OperationContext;
import com.gridnine.elsa.server.core.storage.StorageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdUpdaterInterceptor implements StorageInterceptor {
    @Autowired
    private IdGenerator idGenerator;

    @Override
    public double getPriority() {
        return 0;
    }

    @Override
    public <A extends BaseAsset> void onSave(A asset, OperationContext<A> context) {
        if(asset.getId() == -1){
            asset.setId(idGenerator.nextId());
        } else {
            idGenerator.ensureIdRegistered(asset.getId());
        }
    }

}
