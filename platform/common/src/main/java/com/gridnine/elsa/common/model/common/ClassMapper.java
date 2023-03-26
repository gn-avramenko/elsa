/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.common;

import com.gridnine.elsa.meta.config.Environment;

public interface ClassMapper {
    int getId(String name);
    String getName(int id);

    public static ClassMapper get(){
        return Environment.getPublished(ClassMapper.class);
    }
}
