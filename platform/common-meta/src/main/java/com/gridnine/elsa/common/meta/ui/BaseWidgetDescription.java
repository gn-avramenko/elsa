/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

abstract class BaseWidgetDescription {
    private boolean notEditable;

    abstract public WidgetType getWidgetType();

    public boolean isNotEditable() {
        return notEditable;
    }

    public void setNotEditable(boolean notEditable) {
        this.notEditable = notEditable;
    }
}
