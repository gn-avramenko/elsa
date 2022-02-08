/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class GridContainerCellDescription extends BaseModelElementDescription {
    private String caption;
    private int colspan = 1;

    private BaseWidgetDescription widget;

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public BaseWidgetDescription getWidget() {
        return widget;
    }

    public void setWidget(BaseWidgetDescription widget) {
        this.widget = widget;
    }
}
