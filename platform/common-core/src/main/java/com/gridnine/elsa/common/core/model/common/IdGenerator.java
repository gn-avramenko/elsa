/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

public interface IdGenerator {
    long nextId();
    void ensureIdRegistered(long id);
}
