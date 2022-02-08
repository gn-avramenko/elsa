/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class TableColumnDescription extends BaseModelElementDescription {
    private String prefWidth;
    private BaseWidgetDescription widget;

    public BaseWidgetDescription getWidget() {
        return widget;
    }

    public void setWidget(BaseWidgetDescription widget) {
        this.widget = widget;
    }

    public String getPrefWidth() {
        return prefWidth;
    }

    public void setPrefWidth(String prefWidth) {
        this.prefWidth = prefWidth;
    }


}
