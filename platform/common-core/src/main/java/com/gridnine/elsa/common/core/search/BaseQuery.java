/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseQuery {
    private String freeText;
    private final List<SearchCriterion> criterions = new ArrayList<>();

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public List<SearchCriterion> getCriterions() {
        return criterions;
    }
}
