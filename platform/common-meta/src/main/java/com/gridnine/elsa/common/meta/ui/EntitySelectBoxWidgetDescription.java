/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class EntitySelectBoxWidgetDescription extends BaseWidgetDescription{

    private String objectId;

    @Override
    public WidgetType getWidgetType() {
        return WidgetType.ENTITY_SELECT_BOX;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
