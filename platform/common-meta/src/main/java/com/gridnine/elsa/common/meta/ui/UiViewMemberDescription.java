/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;
import com.gridnine.elsa.common.meta.common.XmlNode;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class UiViewMemberDescription extends BaseElementWithId {

    private String widgetClass;

    public UiViewMemberDescription() {
    }

    public UiViewMemberDescription(String id) {
        super(id);
    }


    public String getWidgetClass() {
        return widgetClass;
    }

    public void setWidgetClass(String widgetClass) {
        this.widgetClass = widgetClass;
    }
}
