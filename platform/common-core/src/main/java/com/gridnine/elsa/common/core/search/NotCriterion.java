/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;

public class NotCriterion extends SearchCriterion{

    public final SearchCriterion criterion;

    public NotCriterion(SearchCriterion criterion) {
        this.criterion = criterion;
    }

    @Override
    public String toString() {
        return "NOT (%s)".formatted(criterion);
    }

}
