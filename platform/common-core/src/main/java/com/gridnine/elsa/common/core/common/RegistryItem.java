/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.common;

public interface RegistryItem<T> {

    RegistryItemType<T> getType();

    String getId();
}
