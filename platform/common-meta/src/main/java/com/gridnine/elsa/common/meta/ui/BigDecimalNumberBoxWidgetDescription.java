/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class BigDecimalNumberBoxWidgetDescription extends BaseWidgetDescription{
    private int precision = 2;
    private boolean nullable;
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.BIG_DECIMAL_NUMBER_BOX;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
