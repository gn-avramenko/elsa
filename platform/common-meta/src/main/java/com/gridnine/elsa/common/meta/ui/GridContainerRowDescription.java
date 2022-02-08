/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class GridContainerRowDescription {
    private PredefinedRowHeight height;
    private String customHeight;

    public PredefinedRowHeight getHeight() {
        return height;
    }

    public void setHeight(PredefinedRowHeight height) {
        this.height = height;
    }

    public String getCustomHeight() {
        return customHeight;
    }

    public void setCustomHeight(String customHeight) {
        this.customHeight = customHeight;
    }
}
