/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class GridContainerColumnDescription {
    private  PredefinedColumnWidth predefinedWidth;
    private String customWidth;

    public PredefinedColumnWidth getPredefinedWidth() {
        return predefinedWidth;
    }

    public void setPredefinedWidth(PredefinedColumnWidth predefinedWidth) {
        this.predefinedWidth = predefinedWidth;
    }

    public String getCustomWidth() {
        return customWidth;
    }

    public void setCustomWidth(String customWidth) {
        this.customWidth = customWidth;
    }
}
