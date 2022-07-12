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

public class UiViewDescription extends BaseElementWithId {
    public UiViewDescription() {
    }

    public UiViewDescription(String id) {
        super(id);
    }

    private final Map<String, Map<Locale, String>> localizations = new LinkedHashMap<>();

    private XmlNode view;

    public Map<String, Map<Locale, String>> getLocalizations() {
        return localizations;
    }

    public XmlNode getView() {
        return view;
    }

    public void setView(XmlNode view) {
        this.view = view;
    }
}
