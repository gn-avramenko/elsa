/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.domain;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.Xeption;

public class _CachedEntityReference<T extends BaseIdentity> extends EntityReference<T> implements CachedObject{

    private boolean allowChanges;

    @Override
    public void setAllowChanges(boolean allowChanges) {
        this.allowChanges = allowChanges;
    }

    @Override
    public void setId(long id) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.setId(id);
    }

    @Override
    public void setType(Class<T> type) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.setType(type);
    }

    @Override
    public void setCaption(String caption) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.setCaption(caption);
    }
}
