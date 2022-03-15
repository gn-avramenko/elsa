/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.lock;

public interface LockManager {
    NamedLock getLock(String name);
}
