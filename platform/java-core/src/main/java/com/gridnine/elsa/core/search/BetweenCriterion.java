/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.search;

public class BetweenCriterion extends SearchCriterion{

    public final String property;

    public final Object lo;

    public final Object hi;

    public BetweenCriterion(String property, Object lo, Object hi) {
        this.property = property;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    public String toString() {
        return "%s BETWEEN %s AND %s".formatted(property, SearchUtils.valueToString(lo), SearchUtils.valueToString(hi));
    }

}
