/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.search;

public class DynamicCriterion extends SearchCriterion{

    public final String handlerId;

    public final String propertyId;

    public final String conditionId;

    public final Object value;

    public DynamicCriterion(String handlerId, String propertyId, String conditionId, Object value) {
        this.handlerId = handlerId;
        this.propertyId = propertyId;
        this.conditionId = conditionId;
        this.value = value;
    }

    @Override
    public String toString() {
        return "%s %s %s".formatted(propertyId, conditionId, SearchUtils.valueToString(value));
    }
}
