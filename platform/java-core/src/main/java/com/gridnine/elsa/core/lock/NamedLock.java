/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.lock;

import java.util.concurrent.locks.Lock;

public interface NamedLock extends Lock,AutoCloseable {
    String getName();
}
