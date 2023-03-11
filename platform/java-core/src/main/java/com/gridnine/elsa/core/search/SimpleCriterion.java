/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.search;

public class SimpleCriterion extends SearchCriterion{

    public final String property;

    public final Operation operation;

    public final Object value;

    public SimpleCriterion(String property, Operation operation, Object value) {
        this.property = property;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public String toString() {
        return "%s %s %s".formatted(property, operation.name(), SearchUtils.valueToString(value));
    }

    public enum Operation {
        EQ,
        NE,
        LIKE,
        ILIKE,
        GT,
        LT,
        GE,
        LE,
        CONTAINS,
    }
}
