/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.domain;


import com.gridnine.elsa.core.model.common.Xeption;

public class _CachedVersionInfo extends VersionInfo implements CachedObject {

    private boolean allowChanges;

    @Override
    public void setAllowChanges(boolean allowChanges) {
        this.allowChanges = allowChanges;
    }

    @Override
    public void setComment(String comment) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.setComment(comment);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if(!allowChanges){
            throw Xeption.forDeveloper("changes are not allowed");
        }
        super.setValue(propertyName, value);
    }
}
