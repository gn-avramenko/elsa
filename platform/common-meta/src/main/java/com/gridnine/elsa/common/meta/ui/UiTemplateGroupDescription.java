/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

import java.util.ArrayList;
import java.util.List;

public class UiTemplateGroupDescription extends BaseElementWithId {
    private final List<UiRefTagDescription> widgets = new ArrayList<>();
    private final List<UiRefTagDescription> views = new ArrayList<>();

    public UiTemplateGroupDescription() {
    }

    public UiTemplateGroupDescription(String id) {
        super(id);
    }

    public List<UiRefTagDescription> getWidgets() {
        return widgets;
    }

    public List<UiRefTagDescription> getViews() {
        return views;
    }
}
