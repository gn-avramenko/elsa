/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class TextBoxWidgetDescription extends BaseWidgetDescription{
    private boolean multiline;
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.TEXT_BOX;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }
}
