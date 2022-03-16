/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;

import com.gridnine.elsa.common.core.common.RegistryItem;
import com.gridnine.elsa.common.core.common.RegistryItemType;

public abstract class DynamicCriterionCondition implements RegistryItem<DynamicCriterionCondition> {
    public static RegistryItemType<DynamicCriterionCondition> TYPE = new RegistryItemType<>("dynamic-criterion-condition");

    public abstract String getDisplayName();

    @Override
    public RegistryItemType<DynamicCriterionCondition> getType(){
        return TYPE;
    }
}
