/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;


import java.util.Collection;

public abstract class DynamicCriterionHandler<T> {

    public abstract boolean isApplicable(String listId, String propertyId);

    public abstract String getDisplayName();

    public abstract Collection<String> getConditionsIds();

    public abstract SearchCriterion getCriterion(String listId, String propertyId, String conditionId, T value);

    public abstract String getValueType();

}
