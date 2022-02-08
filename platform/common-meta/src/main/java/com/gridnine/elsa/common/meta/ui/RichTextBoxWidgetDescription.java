/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class RichTextBoxWidgetDescription extends BaseWidgetDescription{
    private String height;
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.RICH_TEXT_EDITOR;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
