/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class EnumSelectBoxWidgetDescription extends BaseWidgetDescription{

    private String enumId;

    @Override
    public WidgetType getWidgetType() {
        return WidgetType.ENUM_SELECT_BOX;
    }

    public String getEnumId() {
        return enumId;
    }

    public void setEnumId(String enumId) {
        this.enumId = enumId;
    }
}
