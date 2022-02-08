/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class HiddenWidgetDescription extends BaseWidgetDescription{

    private String objectId;
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.HIDDEN;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
