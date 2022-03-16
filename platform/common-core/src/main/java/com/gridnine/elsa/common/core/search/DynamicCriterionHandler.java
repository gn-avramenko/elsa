/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;

import com.gridnine.elsa.common.core.common.RegistryItem;
import com.gridnine.elsa.common.core.common.RegistryItemType;

import java.util.Collection;

public abstract class DynamicCriterionHandler<T> implements RegistryItem<DynamicCriterionHandler<?>> {
    public static RegistryItemType<DynamicCriterionHandler<?>> TYPE = new RegistryItemType<>("dynamic-criterion-handler");

    public abstract boolean isApplicable(String listId, String propertyId);

    public abstract String getDisplayName();

    public abstract Collection<String> getConditionsIds();

    public abstract SearchCriterion getCriterion(String listId, String propertyId, String conditionId, T value);

    public abstract String getValueType();

    @Override
    public RegistryItemType<DynamicCriterionHandler<?>> getType(){
        return  TYPE;
    }
}
