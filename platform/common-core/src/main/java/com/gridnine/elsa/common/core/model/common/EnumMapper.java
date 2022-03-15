/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

public interface EnumMapper {
    int getId(Enum<?> value);
    String getName(int id, Class<Enum<?>> cls);
}
