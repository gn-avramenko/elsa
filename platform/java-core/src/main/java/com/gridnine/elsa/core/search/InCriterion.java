/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.search;

import java.util.List;

public class InCriterion<T> extends SearchCriterion{

    public final String property;

    public final List<T> values;

    public InCriterion(String property,  List<T> values) {
        this.property = property;
        this.values = values;
    }

    @Override
    public String toString() {
        return "%s IN %s".formatted(property, values.stream().map(SearchUtils::valueToString).toList());
    }

}
