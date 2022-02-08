/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class ImageBoxWidgetDescription extends BaseWidgetDescription{
    private Integer maxImageHeight;
    private Integer maxImageWidth;
    private String accept;

    @Override
    public WidgetType getWidgetType() {
        return WidgetType.IMAGE_BOX;
    }

    public Integer getMaxImageHeight() {
        return maxImageHeight;
    }

    public void setMaxImageHeight(Integer maxImageHeight) {
        this.maxImageHeight = maxImageHeight;
    }

    public Integer getMaxImageWidth() {
        return maxImageWidth;
    }

    public void setMaxImageWidth(Integer maxImageWidth) {
        this.maxImageWidth = maxImageWidth;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }
}
