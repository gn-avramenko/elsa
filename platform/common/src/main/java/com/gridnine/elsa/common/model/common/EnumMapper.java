/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

import com.gridnine.elsa.meta.config.Environment;

public interface EnumMapper {
    int getId(Enum<?> value);
    String getName(int id, Class<Enum<?>> cls);

    public static EnumMapper get(){
        return Environment.getPublished(EnumMapper.class);
    }
}
