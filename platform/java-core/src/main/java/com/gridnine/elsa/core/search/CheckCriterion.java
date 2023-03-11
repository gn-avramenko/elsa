/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.search;

public class CheckCriterion extends SearchCriterion {

    public final String property;

    public final Check check;

    public CheckCriterion(String property, Check check) {
        this.property = property;
        this.check = check;
    }

    @Override
    public String toString() {
        return "%s %s".formatted(property, check.name());
    }

    public enum Check {
        IS_EMPTY,
        NOT_EMPTY,
        IS_NULL,
        IS_NOT_NULL,
    }
}
