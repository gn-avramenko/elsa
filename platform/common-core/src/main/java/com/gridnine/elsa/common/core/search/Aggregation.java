/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;

public record Aggregation(String property, Operation operation) {

    @Override
    public String toString() {
        return switch (operation) {
            case MAX -> "max(%s)".formatted(property);
            case MIN -> "min(%s)".formatted(property);
            case AVG -> "avg(%s)".formatted(property);
            case SUM -> "sum(%s)".formatted(property);
            case COUNT -> "count(%s)".formatted(property);
            case PROPERTY -> property;
        };
    }

    public enum Operation {
        MAX,
        MIN,
        AVG,
        SUM,
        COUNT,
        PROPERTY
    }

}
