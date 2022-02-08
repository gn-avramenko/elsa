/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class IntegerNumberBoxWidgetDescription extends BaseWidgetDescription{
    private boolean nullable;
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.INTEGER_NUMBER_BOX;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
