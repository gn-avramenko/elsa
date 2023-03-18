/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.common;

import com.gridnine.elsa.meta.config.Environment;

public interface IdGenerator {
    long nextId();
    void ensureIdRegistered(long id);

    static IdGenerator get(){
        return Environment.getPublished(IdGenerator.class);
    }
}
