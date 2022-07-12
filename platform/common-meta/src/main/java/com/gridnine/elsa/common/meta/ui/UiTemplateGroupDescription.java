/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

import java.util.ArrayList;
import java.util.List;

public class UiTemplateGroupDescription extends BaseElementWithId {
    private final List<String> elements = new ArrayList<>();

    public UiTemplateGroupDescription() {
    }

    public UiTemplateGroupDescription(String id) {
        super(id);
    }


    public List<String> getElements() {
        return elements;
    }
}
